//**************************************************************************
//--------------------------------------------------------------------------
// ercesiMIPS Single Cycle Processor ALU for 9(11) instructions
//
// Meng zhang
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
// The supported instruction is:
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


