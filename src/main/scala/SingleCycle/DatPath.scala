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
	val ctl  		= new CtltoDatIo().flip()
	val dat  		= new DatToCtlIo()
}

class DatPath extends Module {
	val io = IO(new DPathIo ())

	val alu9 = Module(new ALU9())
	//
	// add your code
	// 
}