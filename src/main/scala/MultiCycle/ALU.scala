//**************************************************************************
//--------------------------------------------------------------------------
// ercesiMIPS Multi Cycle Processor ALU for 9 instructions
//
// Meng Zhang
// version 0.1
//--------------------------------------------------------------------------
//**************************************************************************

package MultiCycle

import chisel3._


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

object ALU
{
	def FN_AND	= 0.U(3.W)	//0000
	def FN_OR	= 1.U(3.W)	//0001
	def FN_ADD	= 2.U(3.W)	//0010
	def FN_SUB	= 6.U(3.W)	//0110
	def FN_SLT	= 7.U(3.W)	//0111

	def FN_BEQ	= FN_SUB	//0111

    def isSub(cmd: UInt) = (cmd === FN_SUB) || (cmd === FN_SLT)
}

import ALU._

class ALU extends Module{
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
