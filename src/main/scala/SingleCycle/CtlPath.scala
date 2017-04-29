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
//import chisel3.iotesters.Driver
//import utils.ercesiMIPSRunner
class CtltoDatIo extends Bundle()
{
	val nPC_MUX_sel = Output(Bool())
	val RegWr		= Output(Bool())
	val RegDst		= Output(Bool())
	val ExtOp		= Output(Bool())
	val ALUctr		= Output(UInt(2.W))
	val ALUsrc		= Output(Bool())
	val MemtoReg	= Output(Bool())
	//val MemWr		= Output(Bool())
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
	val dat 		= new DatToCtlIo().flip()
}

class CtlPath extends Module()
{
	val io 			= IO(new CPathIo ())
// add your code
}