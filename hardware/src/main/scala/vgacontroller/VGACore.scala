package io

import patmos.Constants._
import ocp._

import chisel3._

import ocp.{OcpCoreSlavePort, _}

import VGAController._
import java.util.ResourceBundle

class VGACore(extmem_addr_width: Int, data_width: Int, burst_length: Int) extends Module {
  val io = IO(new Bundle() with patmos.HasPins {
    val pins = new Bundle() {
      val pixel_clock = Output(UInt(1.W))
      val n_sync = Output(UInt(1.W))
      val n_blank = Output(UInt(1.W))

      val h_sync = Output(UInt(1.W))
      val v_sync = Output(UInt(1.W))

      val R = Output(UInt(8.W))
      val G = Output(UInt(8.W))
      val B = Output(UInt(8.W))
    }
    val memPort = new OcpBurstMasterPort(extmem_addr_width, data_width, burst_length)
  })

  io.pins.n_sync := 0.U // Pulled to 0 because sync using green channel not use

  val controller = Module(new VGAController(extmem_addr_width, data_width, burst_length))

  io.pins.pixel_clock := controller.io.pixel_clock
  io.pins.n_blank := controller.io.n_blank
  io.pins.h_sync := controller.io.h_sync
  io.pins.v_sync := controller.io.v_sync

  io.pins.R := controller.io.R
  io.pins.G := controller.io.G
  io.pins.B := controller.io.B

  // Connect to Memory
  io.memPort <> controller.io.memPort
}