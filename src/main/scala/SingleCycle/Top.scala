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
	cpath.io.boot := io.boot

	val imm = Mem(256, UInt(32.W))
	val dmm = Mem(1024, UInt(32.W))
	io.test_dm_out := 0.U
	io.test_im_out := 0.U
	cpath.io.Inst := 0.U
	when (io.boot && io.test_im_wr){
		imm(io.test_im_addr >> 2) := io.test_im_in
		cpath.io.Inst := 0.U
	 } 
	when (io.boot && io.test_dm_wr){
		dmm(io.test_dm_addr >> 2) := io.test_dm_in
		cpath.io.Inst := 0.U
	} 
	when (io.boot && io.test_im_rd){
		io.test_im_out := imm(io.test_im_addr >> 2)
		cpath.io.Inst := 0.U
	} 
	when (io.boot && io.test_dm_rd){
		io.test_dm_out := dmm(io.test_dm_addr >> 2)
		cpath.io.Inst := 0.U
	} 
	when (!io.boot){
		cpath.io.Inst := Mux(io.boot, 0.U, imm(dpath.io.imem_addr >> 2))
		dpath.io.dmem_datOut := dmm(dpath.io.dmem_addr >> 2)
		when (cpath.io.MemWr) {
			dmm(dpath.io.dmem_addr >> 2) := dpath.io.dmem_datIn
		}
	}

	val clk_cnt = RegInit(0.U(32.W))
	clk_cnt := clk_cnt + 1.U
	
	printf("Cyc=%d, pc=0x%x, Inst=0x%x, boot=%d, dmem_in = 0x%x, rd_dmm=0x%x, dmm=0x%x\n",
		clk_cnt,
		dpath.io.imem_addr,
		cpath.io.Inst,
		cpath.io.boot,
		dpath.io.dmem_datIn,
		io.test_dm_out,
		dmm(io.test_dm_addr >> 2))
		 //...
}