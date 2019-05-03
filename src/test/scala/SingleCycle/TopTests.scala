//**************************************************************************
//--------------------------------------------------------------------------
// ercesiMIPS Single Cycle Processor ALU Test
//
// Meng zhang
// version 0.1
//--------------------------------------------------------------------------
//**************************************************************************
//  main:
//  	nop
//  	ori		$t1, $zero, 1
//  	ori 	$s1, $zero, 2000
//  	ori 	$s2, $zero, 2004
//  L1:
//  	add	$s1, $s1, $t1
//  	sw 	$t1, 0($zero)
//  	sw	$s2, 4($zero)
//  	sw	$s2, 12($zero)
//  	beq	$s1, $s2, L2
//  	j	L1
//  L2:
//  	nop
//  	nop
// 0x00000000 00000000
// 0x00000001 34090001 ori	$t1, $zero, 1
// 0x00000002 341107d0 ori 	$s1, $zero, 2000
// 0x00000003 341207d4 ori 	$s2, $zero, 2004
// 0x00000004 02298820 add	$s1, $s1, $t1
// 0x00000000 ac090000 sw 	$t1, 0($zero)
// 0x00000000 ac110004 sw	$s1, 4($zero)
// 0x00000000 ac120008 sw	$s2, 8($zero)
// 0x00000000 12320001 beq	$s1, $s2, 1
// 0x00000000 08000001 j	1    // 0000 0100 0000 0000 0000 0000 0000 0001
// 0x00000000 00000000
// 0x00000000 00000000

package SingleCycle

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
		val filename = "inst.s"
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
