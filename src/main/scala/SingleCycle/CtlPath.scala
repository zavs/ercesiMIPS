//**************************************************************************
//--------------------------------------------------------------------------
// ercesiMIPS Single Cycle Processor Control path
//
// Meng zhang
// version 0.1
//--------------------------------------------------------------------------
//**************************************************************************

package SingleCycle

import chisel3._
import chisel3.util._

class CtltoDatIo extends Bundle()
{
	val nPC_sel = Output(Bool())
	val RegWr		= Output(Bool())
	val RegDst		= Output(Bool())
	val ExtOp		= Output(Bool())
	val ALUctr		= Output(UInt(3.W))
	val ALUsrc		= Output(Bool())
	val MemtoReg	= Output(Bool())
	val Rd 			= Output(UInt(5.W))
	val Rt 			= Output(UInt(5.W))
	val Rs 			= Output(UInt(5.W))
	val Imm16 		= Output(UInt(16.W))
	val	Imm26		= Output(UInt(26.W))
}

class CPathIo extends Bundle()
{
	val Inst 		= Input(UInt(32.W))
	val boot		= Input(Bool())
	val MemWr		= Output(Bool())
	val valid		= Output(Bool())
	val ctl 		= new CtltoDatIo()
	val dat 		= Flipped(new DatToCtlIo)
}

class CtlPath extends Module()
{
	val io 			= IO(new CPathIo ())
	// Add your code here. You can init all control signals first.
	// Then decode these signals according to current instruction.
	io.MemWr := 0.U
	io.valid := 1.U
	io.ctl.RegWr := false.B
	val ctl_signle 	= Wire(UInt(11.W))
	//val nPC_sel 	= Wire(Bool())
	val Jump 		= Wire(Bool())
	val ALUop		= Wire(UInt(3.W))
	val ALUctr2		= Wire(Bool())
	val ALUctr1		= Wire(Bool())
	val ALUctr0		= Wire(Bool())


	// val clk_cnt = RegInit(0.U(32.W))
	// clk_cnt := clk_cnt + 1.U
	io.ctl.RegDst 		:= ctl_signle(10)
	io.ctl.ALUsrc 		:= ctl_signle(9)
	io.ctl.MemtoReg		:= ctl_signle(8)
	io.ctl.RegWr		:= ctl_signle(7)
	io.MemWr			:= ctl_signle(6)
	io.ctl.nPC_sel		:= Mux(io.boot, false.B, 
						Mux(Jump, ctl_signle(5), (io.dat.zero & ctl_signle(5))))
	Jump				:= ctl_signle(4)
	io.ctl.ExtOp		:= ctl_signle(3)
	ALUop				:= ctl_signle(2,0)

	ctl_signle := Mux(io.boot, "b00000001000".U(11.W), 
			MuxCase("b11111111111".U(11.W), Array(
			(io.Inst(31, 26) === "b000000".U(6.W) && io.Inst =/= 0.U) 
												   -> "b10010000100".U(11.W), //R
			(io.Inst(31, 26) === "b001101".U(6.W)) -> "b01010000010".U(11.W), //ori
			(io.Inst(31, 26) === "b100011".U(6.W)) -> "b01110001000".U(11.W), //lw
			(io.Inst(31, 26) === "b101011".U(6.W)) -> "b01001001000".U(11.W), //sw
			(io.Inst(31, 26) === "b000100".U(6.W)) -> "b00000101001".U(11.W), //beq
			(io.Inst(31, 26) === "b000010".U(6.W)) -> "b00000110111".U(11.W), //jump
			(io.Inst === 0.U) 					   -> 0.U)))
			//(io.boot)							   -> "b00000100000".U(11.W))) //pc_next-->0

	io.valid := Mux(ctl_signle === "b11111111111".U(11.W), false.B, true.B)

	ALUctr2 := ~ALUop(2) & ALUop(0) | (ALUop(2) & ~io.Inst(2) 
						& io.Inst(1) & ~io.Inst(0))
	ALUctr1 := ~ALUop(2) & ~ALUop(1) | (ALUop(2) & ~io.Inst(2) & ~io.Inst(0))
	ALUctr0 := ~ALUop(2) & ALUop(1) |
						(ALUop(2) & ~io.Inst(3) & io.Inst(2) & ~io.Inst(1) & io.Inst(0)) |
						(ALUop(2) & io.Inst(3) & ~io.Inst(2) & io.Inst(1) & ~io.Inst(0))
	io.ctl.ALUctr := Mux(ctl_signle === "b00000001000".U(11.W), "b011".U,
						Mux(io.Inst === 0.U, 0.U, Cat(ALUctr2, ALUctr1, ALUctr0)))


	io.ctl.Imm16 := Mux(((ALUop === 4.U) || (Jump === true.B)), 0.U, io.Inst(15, 0))
	io.ctl.Imm26 := Mux((Jump === true.B), io.Inst(25, 0), 0.U)

	io.ctl.Rs := io.Inst(25, 21)
	io.ctl.Rt := io.Inst(20, 16)
	io.ctl.Rd := io.Inst(15, 11)
// add your code
	// printf("Cyc=%d, Inst= 0x%x, boot= %x, Ctlpath.ExtOp= %x, Ctlpath.nPC_sel= %x\n",
	// 	clk_cnt, 
	// 	io.Inst,
	// 	io.boot,
	// 	io.ctl.ExtOp,
	// 	io.ctl.nPC_sel)
}