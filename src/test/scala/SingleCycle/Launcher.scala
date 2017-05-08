// See LICENSE.txt for license details.
//**************************************************************************
//--------------------------------------------------------------------------
// ercesiMIPS Single Cycle Processor Test Launcher
//
// Meng zhang
// version 0.1
//--------------------------------------------------------------------------
//**************************************************************************

package SingleCycle

import chisel3._
import chisel3.iotesters.Driver
import utils.ercesiMIPSRunner

object Launcher {
  val tests = Map(
    
    "ALU" -> { (backendName: String) =>
      Driver(() => new ALU(), backendName) {
        (c) => new ALUTests(c)
      }
    },
    "Top" -> { (backendName: String) =>
      Driver(() => new Top(), backendName) {
        (c) => new TopTests(c)
      }
    }
    /*，
    "CtlPath" -> { (backendName: String) =>
      Driver(() => new CtlPath(), backendName) {
        (c) => new CtlPathTests(c)
      }
    }*/
    /*,
    "ALU11" -> { (backendName: String) =>
      Driver(() => new ALU11(), backendName) {
        (c) => new ALU11Tests(c)
      }
    },
    "Top" -> { (backendName: String) =>
      Driver(() => new Top(), backendName) {
        (c) => new TopTests(c)
      }
    }*/
  )

  def main(args: Array[String]): Unit = {
    ercesiMIPSRunner(tests, args) 
  }
}
