package simple

import chisel3._
import chisel3.iotesters.PeekPokeTester
import io.VGACore

import ocp.{OcpCoreSlavePort, _}


class VGACoreTestBench() extends Module {
  val io = IO(new Bundle {
  })

  val core = Module(new VGACore(32, 32, 4)) 

  val count = RegInit(0.U(10.W))

  core.io.memPort.S.CmdAccept := false.B
  core.io.memPort.S.DataAccept := false.B
  core.io.memPort.S.Resp := OcpResp.NULL
  core.io.blank := false.B

  when(core.io.memPort.M.Cmd === OcpCmd.RD) {
    count := 8.U
  }

  when(count > 0.U) {
    count := count - 1.U
  }

  when(count === 5.U) {
    core.io.memPort.S.CmdAccept := true.B
  }

  core.io.memPort.S.Data := "hFFFF0000".U

  when(count > 0.U && count < 5.U) {
    core.io.memPort.S.Resp := OcpResp.DVA
  }
}

/**
 * Test the VGACore design
 */
class VGACoreTester(dut: VGACoreTestBench) extends PeekPokeTester(dut) {
  step(20000)
}


object VGACoreTest extends App {
  iotesters.Driver.execute(Array("--target-dir", "generated", "--generate-vcd-output", "on"), () => new VGACoreTestBench()) {
    c => new VGACoreTester(c)
  }
}