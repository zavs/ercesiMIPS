//**************************************************************************
//--------------------------------------------------------------------------
// ercesiMIPS Single Cycle Processor Data path
//
// Meng zhang
// version 0.1
//--------------------------------------------------------------------------
//**************************************************************************

package SingleCycle

import chisel3._
import chisel3.util._

//import chisel3.iotesters.Driver
//import utils.ercesiMIPSRunner
class DatToCtlIo extends Bundle()
{
	val zero = Output(Bool())
	//val Imm32	= Output(UInt(32.W))
	//val nPC_sel	= Output(Bool())
}

class DPathIo extends Bundle()
{
	//val host		= new HTIFIO()
	val imem_addr	= Output(UInt(32.W))
	val dmem_addr	= Output(UInt(32.W))
	val dmem_datIn	= Output(UInt(32.W))
	val dmem_datOut	= Input(UInt(32.W))
	val ctl  		= Flipped(new CtltoDatIo)
	val dat  		= new DatToCtlIo()
}

class DatPath extends Module {
	val io = IO(new DPathIo ())

	// Internal Signal
	val BusA 	= Wire(UInt(32.W))
	val BusB 	= Wire(UInt(32.W))
	val BusWr 	= Wire(UInt(32.W))
	val pc_next = Wire(UInt(32.W))
	val pc_plus4= Wire(UInt(32.W))
	val pc_br 	= Wire(UInt(32.W))

	val alu9 = Module(new ALU9())

	val pc_reg = RegNext(next = pc_next, init=0.U(32.W))

	val RegFile = Mem(32, UInt(32.W))

	// Zero reg always zero
	RegFile(0) := 0.U

	// Read Register
	BusA := RegFile(io.ctl.Rs)
	BusB := RegFile(io.ctl.Rt)

	val reg_indx = Mux(io.ctl.RegDst, io.ctl.Rd, io.ctl.Rt)

	// Write Register
	when (io.ctl.RegWr === true.B) {
		RegFile(reg_indx) := BusWr
	}

	BusWr := Mux(io.ctl.MemtoReg, io.dmem_datOut, alu9.io.ALUout)

//----------------------------------------//
//---------------next pc unit-------------//
//----------------------------------------//
	val br_sext= Cat(Fill(15, io.ctl.Imm16(15)), io.ctl.Imm16(14, 0), Fill(2, io.ctl.Imm16(15)))
	val j_target = Cat(pc_reg(31, 28), io.ctl.Imm26, 0.U(2.W)) 
	val Imm32	= Mux(io.ctl.ExtOp, Cat(Fill(17, io.ctl.Imm16(15)), io.ctl.Imm16(14, 0)),
					Cat("b0".U(16.W), io.ctl.Imm16(15, 0)))
	//val nPC_MUX_sel = alu9.io.cmp_out & io.ctl.nPC_sel
	io.imem_addr := pc_reg
	//pc_reg := pc_next

	pc_plus4 := pc_reg + 4.U(32.W)
	pc_br := pc_reg + br_sext + 4.U(32.W)

	val pc4_en = Mux(io.ctl.ALUctr === "b011".U(3.W), false.B,
				 true.B)

	pc_next := MuxCase(pc_plus4, Array(
						(pc4_en === true.B && 
						 io.ctl.nPC_sel === false.B)	 -> pc_plus4,
						(io.ctl.ExtOp === true.B &&  pc4_en === true.B &&
						 io.ctl.nPC_sel === true.B)		 -> pc_br,
						(io.ctl.ExtOp === false.B && pc4_en === true.B &&
						 io.ctl.nPC_sel === true.B)	 -> j_target,
						(pc4_en === false.B && 
						 io.ctl.nPC_sel === false.B) 	 -> 0.U
						))
//----------------------------------------//
//------------------ALU-------------------//
//----------------------------------------//
	alu9.io.ALUctr 	:= io.ctl.ALUctr
	alu9.io.in1 	:= BusA
	alu9.io.in2 	:= Mux(io.ctl.ALUsrc, Imm32, BusB)

	io.dat.zero 	:= alu9.io.cmp_out
	io.dmem_addr 	:= alu9.io.ALUout
	io.dmem_datIn 	:= BusB

	//println("pc 0x%x", io.imem_addr)
	//
	// add your code
	// 
	// printf("pc=0x%x ExtOp=%x, nPC_MUX_sel=%x ",
	// 	pc_reg,
	// 	io.ctl.ExtOp,
	// 	nPC_MUX_sel)
}