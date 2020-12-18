package simple

import chisel3._
import chisel3.iotesters.PeekPokeTester
import VGACore._


/**
 * Test the VGACore design
 */
class VGACoreTester(dut: VGACore) extends PeekPokeTester(dut) {
  step(200000)
}


object VGACoreTest extends App {
  iotesters.Driver.execute(Array("--target-dir", "generated", "--generate-vcd-output", "on"), () => new VGACore(32, 32, 10)) {
    c => new VGACoreTester(c)
  }
}