package PixelBuffer

import ocp.{OcpCoreSlavePort, _}

import chisel3._
import chisel3.util._
import chisel3.util.experimental.loadMemoryFromFile

class LineMemory() extends Module {
  val size = 800 * 2

  val io = IO(new Bundle {
    val rdAddr = Input(UInt(log2Ceil(size).W)) 
    val rdData = Output(UInt(16.W)) 
    val wrEna = Input(Bool())
    val wrData = Input(UInt(16.W)) 
    val wrAddr = Input(UInt(log2Ceil(size).W))
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
    val h_pos = Input(UInt(log2Ceil(line_width).W))
    val v_pos = Input(UInt(log2Ceil(display_height).W))

    val mem_addr = Output(UInt(extmem_addr_width.W))
    val read = Output(Bool())
    val mem_data = Input(UInt(data_width.W))
  })

  val memory = Module(new LineMemory())

  // Read from Memory
  io.mem_addr  := 0.U
  io.read      := false.B

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