package simple

import chisel3._
import chisel3.iotesters.PeekPokeTester
import VGACore._

import ocp.{OcpCoreSlavePort, _}


class VGACoreTestBench() extends Module {
  val io = IO(new Bundle {
  })

  val core = Module(new VGACore(32, 32, 4)) 

  val count = RegInit(0.U(6.W))
  core.io.memPort.S.CmdAccept := false.B
  core.io.memPort.S.DataAccept := false.B
  core.io.memPort.S.Resp := OcpResp.NULL

  when(core.io.memPort.M.Cmd === OcpCmd.RD) {
    count := 0.U
    core.io.memPort.S.CmdAccept := true.B
  }

  core.io.memPort.S.Data := count

  when(count <= 5.U) {
    count := count + 1.U
  }

  when(count > 0.U && count < 5.U) {
    core.io.memPort.S.Resp := OcpResp.DVA
  }
}

/**
 * Test the VGACore design
 */
class VGACoreTester(dut: VGACoreTestBench) extends PeekPokeTester(dut) {
  step(200000)
}


object VGACoreTest extends App {
  iotesters.Driver.execute(Array("--target-dir", "generated", "--generate-vcd-output", "on"), () => new VGACoreTestBench()) {
    c => new VGACoreTester(c)
  }
}