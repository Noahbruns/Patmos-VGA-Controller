package PixelBuffer

import ocp.{OcpCoreSlavePort, _}

import chisel3._
import chisel3.util._
import chisel3.util.experimental.loadMemoryFromFile

class LineMemory(size: Int, bits: Int) extends Module {
  val io = IO(new Bundle {
    val rdAddr = Input(UInt(log2Ceil(size).W)) 
    val rdData = Output(UInt(bits.W)) 
    val wrEna = Input(Bool())
    val wrData = Input(UInt(bits.W)) 
    val wrAddr = Input(UInt(log2Ceil(size).W))
  })

  val mem = SyncReadMem(size, UInt(bits.W))
  io.rdData := mem.read(io.rdAddr)

  when(io.wrEna) {
    mem.write(io.wrAddr, io.wrData)
  }
}

class PixelBuffer(line_width: Int, display_height: Int, frame_height: Int, frame_width: Int, extmem_addr_width: Int, data_width: Int, burstLength: Int) extends Module {
  val io = IO(new Bundle {
    val new_frame = Input(UInt(1.W))

    val pixel_clock = Input(UInt(1.W))
    val enable = Input(UInt(1.W))
    val R = Output(UInt(8.W))
    val G = Output(UInt(8.W))
    val B = Output(UInt(8.W))
    val h_pos = Input(UInt(log2Ceil(frame_width).W))
    val v_pos = Input(UInt(log2Ceil(frame_height).W))

    val memPort = new OcpBurstMasterPort(extmem_addr_width, data_width, burstLength)
    val blank = Input(Bool())
  })

  val base_address = 800000.U(32.W)

  val memory = Module(new LineMemory(line_width, 32))

  val bytes_per_word  = data_width / 8         // number of bytes in every word
  val bytes_per_pixel = 2                      // number bytes in each pixel
  val pixel_per_word  = 2                      // number bytes in each pixel

  val wordAddress = RegInit(0.U(log2Ceil(line_width / pixel_per_word).W))     //counts the requested Bursts
  val burstCounter = RegInit(0.U(log2Ceil(burstLength).W))   //counts the recieved
  
  object States {
    val idle      = "b00".U(2.W)
    val hold      = "b01".U(2.W)
    val fill      = "b10".U(2.W)
  }
  val State = RegInit(0.U(2.W))

  val read_v_pos = (io.v_pos + 1.U) % frame_height.U           // Next Line to read

  // Disable Write part
  io.memPort.M.Data := 0.U
  io.memPort.M.DataByteEn := 0.U
  io.memPort.M.DataValid := false.B

  memory.io.wrEna := false.B
  memory.io.wrAddr := 0.U
  memory.io.wrData := 0.U

  when(io.h_pos === 0.U && read_v_pos <= display_height.U) {
    wordAddress := 0.U
    State := States.hold
  }

  io.memPort.M.Cmd := OcpCmd.IDLE
  io.memPort.M.Addr := base_address + (read_v_pos * line_width.U * bytes_per_pixel.U) + wordAddress * bytes_per_word.U
  
  //Increment wordAddress, if data is valid and wordAddress smaller than line_width
  when(State === States.hold){
    io.memPort.M.Cmd := OcpCmd.RD

    when(io.memPort.S.CmdAccept === true.B) {
      State := States.fill
    }
    .otherwise {
      State := States.hold
    }
  }
  when(State === States.fill){
    memory.io.wrAddr := (read_v_pos(0) * (line_width.U >> 1)) + Cat(0.U(2.W), wordAddress)
    memory.io.wrData := io.memPort.S.Data

    when(io.memPort.S.Resp =/= OcpResp.NULL) {
      memory.io.wrEna  := true.B
      memory.io.wrData := io.memPort.S.Data
      
      when(burstCounter === (burstLength - 1).U) {
        when (wordAddress + 1.U < (line_width.U >> 1)) {// This word can still be written
          State := States.hold
        }
        .otherwise {
          State := States.idle
        }
      }
      burstCounter := burstCounter + 1.U
      wordAddress := wordAddress + 1.U
    }
  }

  /* Write Out */
  when(io.v_pos(0) === 0.B) { // Switch between Dual Memories
    when(io.enable === 1.U) {
      memory.io.rdAddr := io.h_pos >> 3
    }.otherwise {
      memory.io.rdAddr := line_width.U >> 3 // Prepare for next line
    }
  }.otherwise{
    when(io.enable === 1.U) {
      memory.io.rdAddr := (Cat(0.U(1.W), io.h_pos) + line_width.U) >> 3 // Best approach???
    }.otherwise {
      memory.io.rdAddr := 0.U // Prepare for next line
    }
  }
  val rdData = memory.io.rdData

  io.R := 0.U
  io.G := 0.U
  io.B := 0.U
  when(io.enable === 1.U) {
    var addr  = io.h_pos(2, 0)
    var value = rdData(addr, addr + 3)
    
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