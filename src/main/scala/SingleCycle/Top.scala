//**************************************************************************
//--------------------------------------------------------------------------
// ercesiMIPS Single Cycle Processor Top
//
// Meng zhang
// version 0.1
//--------------------------------------------------------------------------
//**************************************************************************

package SingleCycle

import chisel3._
import chisel3.iotesters.Driver
//import utils.ercesiMIPSRunner
class TopIO extends Bundle() {
	val boot = Input(Bool()) 
// imem and dmem interface for Tests
	val test_im_wr		= Input(Bool())
	val test_im_rd 		= Input(Bool())
	val test_im_addr 	= Input(UInt(32.W))
	val test_im_in 		= Input(UInt(32.W))
	val test_im_out 	= Output(UInt(32.W))

	val test_dm_wr		= Input(Bool())
	val test_dm_rd 		= Input(Bool())
	val test_dm_addr 	= Input(UInt(32.W))
	val test_dm_in 		= Input(UInt(32.W))
	val test_dm_out 	= Output(UInt(32.W))

	val valid			= Output(Bool())
}

class Top extends Module() {
	val io 		= IO(new TopIO())//in chisel3, io must be wrapped in IO(...) 
	val cpath	= Module(new CtlPath())
	val dpath 	= Module(new DatPath())

	cpath.io.ctl <> dpath.io.ctl
	cpath.io.dat <> dpath.io.dat
	io.valid := cpath.io.valid

	val imm = Mem(256, UInt(32.W))
	val dmm = Mem(1024, UInt(32.W))

	when (io.boot && io.test_im_wr){
		imm(io.test_im_addr >> 2) := io.test_im_in
		} .elsewhen (io.boot && io.test_dm_wr){
			dmm(io.test_dm_addr >> 2) := io.test_dm_in
		} .elsewhen (io.boot && io.test_im_rd){
			io.test_im_out := imm(io.test_im_addr >> 2)
		} .elsewhen (io.boot && io.test_dm_rd){
			io.test_dm_out := dmm(io.test_dm_addr >> 2)
		} .elsewhen (!io.boot){
			cpath.io.Inst := Mux(io.boot, 0.U, imm(dpath.io.imem_addr >> 2))
			dpath.io.dmem_datOut := dmm(dpath.io.dmem_addr >> 2)
			when (cpath.io.MemWr) {
				dmm(dpath.io.dmem_addr >> 2) := dpath.io.dmem_datIn
			}
		}
	
		 //...
}