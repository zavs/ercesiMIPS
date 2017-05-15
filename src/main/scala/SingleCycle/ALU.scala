//**************************************************************************
//--------------------------------------------------------------------------
// ercesiMIPS Single Cycle Processor ALU for 9(11) instructions
//
// Meng Zhang
// version 0.1
//--------------------------------------------------------------------------
//**************************************************************************

package SingleCycle

import chisel3._

class ALU extends Module {
	val io = IO(new Bundle {
		val a = Input(UInt(32.W))
		val b = Input(UInt(32.W))
		val op = Input(UInt(4.W))
		val shamt = Input(UInt(5.W))
		val out = Output(UInt(32.W))
		val zero = Output(Bool())
	})

	when ( io.op === 0.U ) {        // AND 	0000
		io.out := io.a & io.b 
	} .elsewhen ( io.op === 1.U ) { // OR 	0001
		io.out := io.a | io.b 
	} .elsewhen ( io.op === 2.U ) { // NOR 	0010
		io.out := ~( io.a | io.b ) 
	} .elsewhen ( io.op === 3.U ) { // XOR 	0011
		io.out := io.a ^ io.b
	} .elsewhen ( io.op === 4.U ) { // ADD 	0100
		io.out := io.a + io.b
	} .elsewhen ( io.op === 5.U ) { // SUB 	0101
		io.out := io.a - io.b
	} .elsewhen ( io.op === 6.U ) { // sll 	0110
		io.out := io.b << io.shamt
	} .elsewhen ( io.op === 7.U ) { // srl 	0111
		io.out := io.b >> io.shamt
	} .elsewhen ( io.op === 8.U ) { // slt 	1000
		io.out := io.a < io.b
	} .elsewhen ( io.op === 9.U ) { // shift immediate by 16 	1001
		io.out := io.b << 16.U
	} .otherwise {
		io.out := 0.U
	}

	io.zero := ( io.a - io.b ) === 0.U
}


// For our first sample ALU with 9 Insts
// The supported instruction is following:
// ADD  000000 sssss ttttt ddddd 00000 100000 //R type (signed)
// SUB  000000 sssss ttttt ddddd 00000 100010 //R type (signed)
// OR   000000 sssss ttttt ddddd 00000 100101 //R type (unsigned)
// AND  000000 sssss ttttt ddddd 00000 100100 //R type (unsigned)
// ORI	001101 sssss ttttt iiiii iiiii iiiiii //I type (unsigned)
// ANDI 001100 sssss ttttt iiiii iiiii iiiiii //I type (unsigned)
// LW   100011 sssss ttttt iiiii iiiii iiiiii //I type (signed imm)
// SW   101011 sssss ttttt iiiii iiiii iiiiii //I type (signed imm)
// BEQ  000100 sssss ttttt iiiii iiiii iiiiii //I type (signed imm)
// SLT  000000 sssss ttttt ddddd 00000 101010 //R type (signed)
// JUMP 000010 iiiii iiiii iiiii iiiii iiiiii //J type (signed imm)
// Although 11 instruction implmented in this class, only
// SUB, ADD, AND, OR, and SLT, BEQ in ALU operations
//---------------------------------------------------//

object ALU9
{
	def FN_AND	= 0.U(3.W)	//000
	def FN_OR	= 1.U(3.W)	//001
	def FN_ADD	= 2.U(3.W)	//010
	def FN_SUB	= 6.U(3.W)	//110
	def FN_SLT	= 7.U(3.W)	//111

	def FN_BEQ	= FN_SUB	//110

    def isSub(cmd: UInt) = (cmd === FN_SUB) || (cmd === FN_SLT)
}

import ALU9._

class ALU9 extends Module{
	val io = IO(new Bundle{
		val in1		= Input(UInt(32.W))
		val in2 	= Input(UInt(32.W))
		val ALUctr 	= Input(UInt(3.W))
		val ALUout	= Output(UInt(32.W))
		val cmp_out	= Output(Bool())
		})

	//val SIntA = SInt(32.W)
	//val SIntB = SInt(32.W)

	// ADD, SUB
	val in2_inv = Mux(isSub(io.ALUctr), ~io.in2, io.in2)
	val in1_xor_in2 = io.in1 ^ io.in2
	val adder_out = io.in1 + in2_inv + isSub(io.ALUctr)

	// SLT and BEQ comparation Output
	// For BEQ, cmp_out = (in1_xor_in2 === 0.U)
	// For SLT, cmp_out = adder_out(31) if io.in1(31) != io.in2(31)
	// Otherwise, cmp_out = adder_out(31)
	io.cmp_out := Mux(io.ALUctr === FN_BEQ, in1_xor_in2 === 0.U, 
		Mux(io.in1(31) != io.in2(31), adder_out(31),
		Mux(adder_out(31), true.B, false.B)))

	// AND, OR, however this can also output XOR
    val logic_out = Mux(io.ALUctr === FN_OR, in1_xor_in2, 0.U) | 
    Mux(io.ALUctr === FN_OR || io.ALUctr === FN_AND, io.in1 & io.in2, 0.U)
    
    val out = Mux(io.ALUctr === FN_ADD || io.ALUctr === FN_SUB, 
    	adder_out, logic_out)

    io.ALUout := out
}
