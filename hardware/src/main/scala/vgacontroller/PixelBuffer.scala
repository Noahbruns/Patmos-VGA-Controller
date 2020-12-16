package PixelBuffer

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

class PixelBuffer() extends Module {
  val line_width = 800
  val display_height = 600

  val io = IO(new Bundle {
    val new_frame = Input(UInt(1.W))

    val pixel_clock = Input(UInt(1.W))
    val enable = Input(UInt(1.W))
    val R = Output(UInt(8.W))
    val G = Output(UInt(8.W))
    val B = Output(UInt(8.W))
    val h_pos = Input(UInt(log2Ceil(line_width).W))
    val v_pos = Input(UInt(log2Ceil(display_height).W))
  })

  val memory = Module(new LineMemory())

  //A few colors
  val lila = RegInit(18532.U(16.W)) 
  val blue = RegInit(13298.U(16.W))
  val green = RegInit(2610.U(16.W)) 
  val yellow = RegInit(59150.U(16.W)) 
  val orange = RegInit(58304.U(16.W)) 
  val red = RegInit(51336.U(16.W))

  memory.io.wrEna  := false.B
  memory.io.wrAddr := 0.U
  memory.io.wrData := 0.U

  //Used to fill the memory 
  val colorCount = RegInit(0.U(log2Ceil(1600).W))
  when(colorCount <= 1600.U){
    memory.io.wrEna  := true.B
    memory.io.wrAddr := colorCount

    switch ((colorCount % 800.U) / 134.U) {
      is (0.U) {
        memory.io.wrData := lila
      }
      is (1.U) {
        memory.io.wrData := blue
      }
      is (2.U) {
        memory.io.wrData := green
      }
      is (3.U) {
        memory.io.wrData := yellow
      }
      is (4.U) {
        memory.io.wrData := orange
      }
      is (5.U) {
        memory.io.wrData := red
      }
    }

    colorCount := colorCount + 1.U
  }

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