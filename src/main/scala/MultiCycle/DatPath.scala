//**************************************************************************
//--------------------------------------------------------------------------
// ercesiMIPS Single Cycle Processor Data path
//
// Meng zhang
// version 0.1
//--------------------------------------------------------------------------
//**************************************************************************

package MultiCycle

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




	//println("pc 0x%x", io.imem_addr)
	//
	// add your code
	//
	// printf("pc=0x%x ExtOp=%x, nPC_MUX_sel=%x ",
	// 	pc_reg,
	// 	io.ctl.ExtOp,
	// 	nPC_MUX_sel)
}
