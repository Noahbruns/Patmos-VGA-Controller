package PixelBuffer

import ocp.{OcpCoreSlavePort, _}

import chisel3._
import chisel3.util._
import chisel3.util.experimental.loadMemoryFromFile

class LineMemory(line_width: Int) extends Module {
  val size = line_width * 2

  val io = IO(new Bundle {
    val rdAddr = Input(UInt(log2Ceil(size + 1).W)) 
    val rdData = Output(UInt(16.W)) 
    val wrEna = Input(Bool())
    val wrData = Input(UInt(16.W)) 
    val wrAddr = Input(UInt(log2Ceil(size + 1).W))
  })

  val mem = SyncReadMem(size, UInt(16.W))
  io.rdData := mem.read(io.rdAddr)

  when(io.wrEna) {
    mem.write(io.wrAddr, io.wrData)
  }
}

class PixelBuffer(line_width: Int, display_height: Int, extmem_addr_width: Int, data_width: Int) extends Module {
  val io = IO(new Bundle {
    val new_frame = Input(UInt(1.W))

    val pixel_clock = Input(UInt(1.W))
    val enable = Input(UInt(1.W))
    val R = Output(UInt(8.W))
    val G = Output(UInt(8.W))
    val B = Output(UInt(8.W))
    val h_pos = Input(UInt(log2Ceil(line_width + 1).W))
    val v_pos = Input(UInt(log2Ceil(display_height + 1).W))

    val mem_addr = Output(UInt(extmem_addr_width.W))
    val mem_read = Output(Bool())
    val mem_valid = Input(Bool())
    val mem_data = Input(UInt(data_width.W))
  })

  val memory = Module(new LineMemory(line_width))

  val recvBuf = RegInit(0.U(data_width.W))

  val lineAddress = RegInit(0.U(log2Ceil(400).W))

  val v_pos_next = RegInit(0.U(log2Ceil(display_height).W))
  v_pos_next := io.v_pos + 1.U
  
  /*when(h_pos === /*Am ende*/){
    v_pos_next := v_pos + 1
  }*/
  val base_address = 0.U
  
  //Requesting data from SRAM 
  recvBuf      := io.mem_data  //Getting two pixels at once
  io.mem_addr  := base_address     //FIXME: Requesting the correct address
  io.mem_read      := true.B

  memory.io.wrEna := true.B
  memory.io.wrAddr := lineAddress //Line 1
  memory.io.wrData := recvBuf

  //Increment lineAddress, if data is valid and lineAddress smaller than 400
  when(io.mem_valid === true.B && lineAddress < 400.U){
    lineAddress := lineAddress + 1.U
  }

  //Reset lineAddress if v_pos toggles
  when(io.h_pos === 0.U){
    lineAddress := 0.U
  }

  // Write to line memory
  when(v_pos_next(0) === 0.B) { // Switch between Dual Memories
      //Write to Line 1
      memory.io.wrAddr := 400.U(10.W) + lineAddress //Line 1
    }.otherwise{
      //Write to Line 0
      memory.io.wrAddr := lineAddress //Line 0
    }

  // Write Out
  when(io.v_pos(0) === 0.B) { // Switch between Dual Memories
    when(io.enable === 1.U) {
      memory.io.rdAddr := io.h_pos
    }.otherwise {
      memory.io.rdAddr := line_width.U // Prepare for next line
    }
  }.otherwise{
    when(io.enable === 1.U) {
      memory.io.rdAddr := Cat(0.U(1.W), io.h_pos) + line_width.U // Best approach???
    }.otherwise {
      memory.io.rdAddr := 0.U // Prepare for next line
    }
  }
  val rdData = memory.io.rdData

  io.R := 0.U
  io.G := 0.U
  io.B := 0.U
  when(io.enable === 1.U) {
    io.R := rdData(14, 10) << 3
    io.G := rdData(9, 5) << 3
    io.B := rdData(4, 0) << 3
  }
}