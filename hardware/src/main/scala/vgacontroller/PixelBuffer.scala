package PixelBuffer

import ocp.{OcpCoreSlavePort, _}

import chisel3._
import chisel3.util._
import chisel3.util.experimental.loadMemoryFromFile

class LineMemory(line_width: Int, bits: Int) extends Module {
  val size = line_width * 2

  val io = IO(new Bundle {
    val rdAddr = Input(UInt(log2Ceil(size + 1).W)) 
    val rdData = Output(UInt(bits.W)) 
    val wrEna = Input(Bool())
    val wrData = Input(UInt(bits.W)) 
    val wrAddr = Input(UInt(log2Ceil(size + 1).W))
  })

  val mem = SyncReadMem(size, UInt(bits.W))
  io.rdData := mem.read(io.rdAddr)

  when(io.wrEna) {
    mem.write(io.wrAddr, io.wrData)
  }
}

class PixelBuffer(line_width: Int, display_height: Int, frame_height: Int, frame_width: Int, extmem_addr_width: Int, data_width: Int, burst_length: Int) extends Module {
  val io = IO(new Bundle {
    val new_frame = Input(UInt(1.W))

    val pixel_clock = Input(UInt(1.W))
    val enable = Input(UInt(1.W))
    val R = Output(UInt(8.W))
    val G = Output(UInt(8.W))
    val B = Output(UInt(8.W))
    val h_pos = Input(UInt(log2Ceil(frame_width).W))
    val v_pos = Input(UInt(log2Ceil(frame_height).W))

    val memPort = new OcpBurstMasterPort(extmem_addr_width, data_width, burst_length)
  })

  val base_address = 0.U

  val memory = Module(new LineMemory(line_width, 32))

  val bytes_per_word  = data_width / 8         // number of bytes in every word
  val bytes_per_pixel = 2                      // number bytes in each pixel
  val pixel_per_word  = 2                      // number bytes in each pixel

  val lineAddress = RegInit(0.U(log2Ceil(line_width / pixel_per_word).W))     //counts the requested Bursts
  val burst_counter = RegInit(0.U(log2Ceil(burst_length).W))   //counts the recieved
  object States {
    val Idle      = "b00".U(2.W)
    val Request   = "b01".U(2.W)
    val Process   = "b10".U(2.W)
  }
  val State = RegInit(0.U(2.W))

  val read_v_pos = (io.v_pos + 1.U) % frame_height.U           // Next Line to read

  io.memPort.M.Cmd := OcpCmd.IDLE
  io.memPort.M.Addr := 0.U
  io.memPort.M.Data := 0.U
  io.memPort.M.DataByteEn := 0.U
  io.memPort.M.DataValid := false.B

  memory.io.wrEna := false.B
  memory.io.wrAddr := 0.U
  memory.io.wrData := 0.U

  when(io.h_pos === 0.U && read_v_pos <= display_height.U) {
    lineAddress := 0.U
    State := States.Request
  }

  //Increment lineAddress, if data is valid and lineAddress smaller than line_width
  when(State === States.Request){
    io.memPort.M.Addr := base_address + (read_v_pos * line_width.U * bytes_per_pixel.U) + lineAddress * bytes_per_word.U
    io.memPort.M.Cmd := OcpCmd.RD

    when(io.memPort.S.CmdAccept === true.B) {
      State := States.Process
    }
  }
  .elsewhen(State === States.Process && io.memPort.S.Resp === OcpResp.DVA){
    memory.io.wrEna  := true.B
    memory.io.wrAddr := (read_v_pos * line_width.U) + lineAddress
    memory.io.wrData := io.memPort.S.Data

    lineAddress := lineAddress + 1.U
    burst_counter := burst_counter + 1.U

    when (burst_counter === (burst_length - 1).U) {
      when (lineAddress * pixel_per_word.U < line_width.U) {
        State := States.Request
      }
      .otherwise {
        State := States.Idle
      }
    }
  }

  /* Write Out */
  when(io.v_pos(0) === 0.B) { // Switch between Dual Memories
    when(io.enable === 1.U) {
      memory.io.rdAddr := io.h_pos >> 1
    }.otherwise {
      memory.io.rdAddr := line_width.U // Prepare for next line
    }
  }.otherwise{
    when(io.enable === 1.U) {
      memory.io.rdAddr := Cat(0.U(1.W), io.h_pos >> 1) + line_width.U // Best approach???
    }.otherwise {
      memory.io.rdAddr := 0.U // Prepare for next line
    }
  }
  val rdData = memory.io.rdData

  io.R := 0.U
  io.G := 0.U
  io.B := 0.U
  when(io.enable === 1.U) {
    when(io.h_pos(0) === 1.U) {
      io.R := rdData(14, 10) << 3
      io.G := rdData(9, 5) << 3
      io.B := rdData(4, 0) << 3
    }.otherwise {
      io.R := rdData(30, 26) << 3
      io.G := rdData(25, 21) << 3
      io.B := rdData(20, 16) << 3
    }
  }
}