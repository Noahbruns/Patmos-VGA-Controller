package VGAController

import chisel3._
import chisel3.util._

import ocp.{OcpCoreSlavePort, _}

import PixelBuffer._

class VGAController(extmem_addr_width: Int, data_width: Int) extends Module {
  val io = IO(new Bundle {
    val n_blank = Output(UInt(1.W))
    val h_sync = Output(UInt(1.W))
    val v_sync = Output(UInt(1.W))
    val new_frame = Output(UInt(1.W))
    val pixel_clock = Output(UInt(1.W))

    val R = Output(UInt(8.W))
    val G = Output(UInt(8.W))
    val B = Output(UInt(8.W))

    val mem_addr = Output(UInt(extmem_addr_width.W))
    val mem_read = Output(Bool())
    val mem_valid = Input(Bool())
    val mem_data = Input(UInt(data_width.W))
  })

  /* Devide clock to generate Pixel clock */
  val cntReg = RegInit(0.U(1.W))
  val pixel_clock = RegInit(false.B)

  // Clock devide by 2
  pixel_clock := ~pixel_clock
  io.pixel_clock := pixel_clock

  io.n_blank := 0.U
  io.h_sync := 0.U
  io.v_sync := 0.U
  io.new_frame := 0.U

  /* Vertical */
  val v_front_porch = 1 // in lines
  val v_back_porch = 23
  val v_sync = 4
  val v_display = 600

  val frame_height = v_display + v_front_porch + v_sync + v_back_porch

  /* Horizontal */
  val h_front_porch = 40
  val h_back_porch = 88
  val h_sync = 128
  val h_display = 800

  val frame_width = h_display + h_front_porch + h_sync + h_back_porch

  val v_cntReg = RegInit(0.U(log2Ceil(frame_height).W))
  val h_cntReg = RegInit(0.U(log2Ceil(frame_width).W))

  /* Generate Pixel buffer */
  val PixelBuffer = Module(new PixelBuffer(h_display, v_display, extmem_addr_width, data_width))
  
  PixelBuffer.io.new_frame := io.new_frame // Add synchronizer
  PixelBuffer.io.enable := io.n_blank
  PixelBuffer.io.pixel_clock := pixel_clock
  PixelBuffer.io.h_pos := h_cntReg
  PixelBuffer.io.v_pos := v_cntReg

  io.mem_addr := PixelBuffer.io.mem_addr
  io.mem_read := PixelBuffer.io.mem_read
  PixelBuffer.io.mem_valid := io.mem_valid
  PixelBuffer.io.mem_data := io.mem_data

  io.R := PixelBuffer.io.R
  io.G := PixelBuffer.io.G
  io.B := PixelBuffer.io.B

  // Generate Counter
  when(~pixel_clock) {
    h_cntReg := h_cntReg + 1.U
  }

  when(h_cntReg === (frame_width - 1).U) {
    h_cntReg := 0.U
    v_cntReg := v_cntReg + 1.U
  }

  when(v_cntReg === frame_height.U) {
    v_cntReg := 0.U
    io.new_frame := 1.U
  }

  // output v_sync
  when (v_cntReg >= (v_display + v_front_porch).U && v_cntReg < (v_display + v_front_porch + v_sync).U) {
    io.v_sync := 1.U
  }
  .otherwise {
    io.v_sync := 0.U
  }

  // output h_sync
  when (h_cntReg >= (h_display + h_front_porch).U && h_cntReg < (h_display + h_front_porch + h_sync).U) {
    io.h_sync := 1.U
  }
  .otherwise {
    io.h_sync := 0.U
  }

  // output n_blank
  when (h_cntReg < h_display.U && v_cntReg < v_display.U) {
    io.n_blank := 1.U
  }
  .otherwise {
    io.n_blank := 0.U
  }
}