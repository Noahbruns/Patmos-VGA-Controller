// Chisel highlighting

/*
 * This code is a minimal hardware described in Chisel.
 *
 * Blinking LED: the FPGA version of Hello World
 */

package VGADriver

import chisel3._

import VGAController._

/*
 * The blinking LED component.
 */

class VGADriver extends Module {
  val io = IO(new Bundle {
    val pixel_clock = Output(UInt(1.W))
    val n_sync = Output(UInt(1.W))
    val n_blank = Output(UInt(1.W))

    val h_sync = Output(UInt(1.W))
    val v_sync = Output(UInt(1.W))

    val R = Output(UInt(8.W))
    val G = Output(UInt(8.W))
    val B = Output(UInt(8.W))
  })

  io.n_sync := 0.U // Pulled to 0 because sync using green channel not use

  val controller = Module(new VGAController())

  io.pixel_clock := controller.io.pixel_clock
  io.n_blank := controller.io.n_blank
  io.h_sync := controller.io.h_sync
  io.v_sync := controller.io.v_sync

  io.R := controller.io.R
  io.G := controller.io.G
  io.B := controller.io.B
}

/**
 * An object extending App to generate the Verilog code.
 */
object VGADriver extends App {
  chisel3.Driver.execute(Array[String](), () => new VGADriver())
}
