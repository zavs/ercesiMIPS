//**************************************************************************
//--------------------------------------------------------------------------
// ercesiMIPS Single Cycle Processor ALU Test
//
// Meng zhang
// version 0.1
//--------------------------------------------------------------------------
//**************************************************************************

package SingleCycle

import chisel3.iotesters.PeekPokeTester

class ALUTests(c: ALU) extends PeekPokeTester(c) {
	val maxInt = 3232
	val maxShamt = 31
	val maxOp = 9
	//var opCode = 0
	var tot = 0
	var zFlag = 0

	poke(c.io.a, 0)
	poke(c.io.b, 0)
	poke(c.io.op, 0)
	poke(c.io.shamt, 0)

	for (i <- 0 until 40){
		val in0 = rnd.nextInt(maxInt)
		val in1 = rnd.nextInt(maxInt)
		val shamt = rnd.nextInt(maxShamt)
		val opCode = rnd.nextInt(10)
		poke(c.io.a, in0)
		poke(c.io.b, in1)
		poke(c.io.op, opCode)
		poke(c.io.shamt, shamt)
		step(1)

		if (opCode == 0){
			tot = in0 & in1
		}else if(opCode == 1){
			tot = in0 | in1
		}else if(opCode == 2) {
			tot = ~(in0 | in1)
		}else if(opCode == 3) {
			tot = in0 ^ in1
		}else if(opCode == 4) {
			tot = in0 + in1
		}else if(opCode == 5) {
			tot = in0 - in1
		}else if(opCode == 6) {
			tot = in1 << shamt
		}else if(opCode == 7) {
			tot = in1 >> shamt
		}else if(opCode == 8) {
			tot = if (in0 < in1) 1 else 0
		}else if(opCode == 9) {
			tot = in1 << 16
		}else tot = 0

		if ((in0-in1) == 0) zFlag = 1
		else zFlag = 0
		//opCode = opCode + 1

		expect(c.io.out, tot)
		expect(c.io.zero, zFlag)

	}
}

// class ALU11Tests(c: ALU11) extends PeekPokeTester(c) {
// 	var taluout = 0
// 	var tcmpout = 0
// 	var getALUout = 0

// 	def asUInt(InInt: Int) = (BigInt(InInt >>> 1) << 1) + (InInt & 1)

// 	for (i <- 0 until 40){
// 		val tin1 = rnd.nextInt(10000000)
// 		val tin2 = rnd.nextInt(10000000)
// 		//val taluctr = rnd.nextInt(7)
// 		val taluctr = 0
// 		poke(c.io.in1, tin1)
// 		poke(c.io.in2, tin2)
// 		poke(c.io.ALUctr, taluctr)

// 		step(1)

// 		if(taluctr == 0){
// 			taluout = tin1 & tin2
// 			tcmpout = 0 
// 		}else if(taluctr == 1){
// 			taluout = tin1 | tin2
// 			tcmpout = 0 
// 		}else if(taluctr == 2){
// 			taluout = tin1 + tin2
// 			tcmpout = 0
// 		}else if(taluctr == 6){
// 			taluout = tin1 - tin2
// 			tcmpout = if(tin1 == tin2) 1 else 0
// 		}else if(taluctr == 7){
// 			taluout = 0
// 			tcmpout = if(tin1 < tin2) 1 else 0
// 		}else {
// 			taluout = 0
// 			tcmpout = if(tin1 < tin2) 1 else 0
// 		}

// 		//getALUout = peek(c.io.ALUout)
// 		if((taluctr == 6) & (tin1 < tin2)){
// 			expect(c.io.ALUout, asUInt(taluout))
// 		}else {
// 			expect(c.io.ALUout, taluout)
// 		}
// 		expect(c.io.cmp_out, tcmpout)
// 	}
// }