//**************************************************************************
//--------------------------------------------------------------------------
// ercesiMIPS Multi Cycle Processor ALU Test
//
// Meng zhang
// version 0.1
//--------------------------------------------------------------------------
//**************************************************************************


package MultiCycle

import chisel3.iotesters.PeekPokeTester
import java.io.PrintWriter
import java.io.File
import scala.io.Source

class TopTests(c: Top) extends PeekPokeTester(c) {
	def asUInt(InInt: Int) = (BigInt(InInt >>> 1) << 1) + (InInt & 1)

	// Reset the CPU
	def TopBoot() = {
		poke(c.io.boot, 1)
		poke(c.io.test_im_wr, 0)
		poke(c.io.test_dm_wr, 0)
		step(1)
	}

	// Initialize the IMM content from file "inst.s", 
	// which could be dumped from MARS.
	def WriteImm () = {
		val filename = "inst2.s"
		var addr = 0
		var Inst = 0
		for (line <- Source.fromFile(filename).getLines){
			Inst = Integer.parseUnsignedInt(line, 16)
			poke(c.io.boot, 1)
			poke(c.io.test_im_wr, 1)
			poke(c.io.test_im_addr, addr*4)
			poke(c.io.test_im_in, asUInt(Inst))
			addr = addr + 1
			step(1)
		}
	}

    // Reset CPU
	TopBoot()

	// Init IMM
	WriteImm()

    // Run the CPU for 100 cycles
    for (i <- 0 until 100){
		poke(c.io.boot, 0)
		poke(c.io.test_im_wr, 0)
		poke(c.io.test_dm_wr, 0)
		expect(c.io.valid, 1)
		step(1)
	}

	// Check the SW instruction in the DMM
	val DmmQ = List(1, 2004, 2004)  // The value is decided by "inst.s"
	for (i <- 0 until 3){
		poke(c.io.boot, 1)
		poke(c.io.test_dm_rd, 1)
		poke(c.io.test_dm_addr, i*4)
		expect(c.io.test_dm_out, DmmQ(i))
		step(1)
	}
}
