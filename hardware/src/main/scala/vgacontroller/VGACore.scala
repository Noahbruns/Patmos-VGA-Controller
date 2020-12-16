// Chisel highlighting

/*
 * This code is a minimal hardware described in Chisel.
 *
 * Blinking LED: the FPGA version of Hello World
 */

package VGACore

import chisel3._

import ocp.{OcpCoreSlavePort, _}

import VGAController._

/*
 * The blinking LED component.
 */

class VGACore(extmem_addr_width: Int, data_width: Int, burst_length: Int) extends Module {
  val io = IO(new Bundle {
    val pixel_clock = Output(UInt(1.W))
    val n_sync = Output(UInt(1.W))
    val n_blank = Output(UInt(1.W))

    val h_sync = Output(UInt(1.W))
    val v_sync = Output(UInt(1.W))

    val R = Output(UInt(8.W))
    val G = Output(UInt(8.W))
    val B = Output(UInt(8.W))

    val memPort = new OcpBurstMasterPort(extmem_addr_width, data_width, burst_length)
  })

  io.n_sync := 0.U // Pulled to 0 because sync using green channel not use

  val controller = Module(new VGAController(extmem_addr_width, data_width))

  io.pixel_clock := controller.io.pixel_clock
  io.n_blank := controller.io.n_blank
  io.h_sync := controller.io.h_sync
  io.v_sync := controller.io.v_sync

  io.R := controller.io.R
  io.G := controller.io.G
  io.B := controller.io.B

  //controller.io.mem_addr := io.memPort.Addr
  //controller.io.read := io.memPort
  //controller.io.mem_data := io.memPort
}
