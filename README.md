ercesiMIPS Processor Porject
====================================

Author: Meng Zhang (zhangm@nwpu.edu.cn)

Author: Jianfeng An (anjf@nwpu.edu.cn)

Author: Danghui Wang (wangdh@nwpu.edu.cn)

Date: 2017 April 9

Diagrams: [ercesiMIPS wiki](http://www.ercesi.org). If you have any questions, share it with us in our [community chat room](https://gitter.im/ercesi/ercesiMIPS).

This repo has been put together to demonstrate a number of simple MIPS Processors written in [Chisel](http://chisel.eecs.berkeley.edu).

### Install Chisel (according to [usb-bar chisel3](https://github.com/ucb-bar/chisel3))
1) [Install sbt](http://www.scala-sbt.org/release/docs/Installing-sbt-on-Linux.html)
Run the following from the terminal to install sbt in (Ubuntu-like) Linux.
```
echo "deb https://dl.bintray.com/sbt/debian /" | sudo tee -a /etc/apt/sources.list.d/sbt.list
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 2EE0EA64E40A89B84B2DF73499E82A75642AC823
sudo apt-get update
sudo apt-get install sbt
```

2) Install updated Verilator. 
Install dependencies: 
```
sudo apt-get install git make autoconf g++ flex bison
```
Clone the Verilator repository：
```
git clone http://git.veripool.org/git/verilator
```
In the Verilator repository directory, check out a known good version (You can skip the step if the latest version is statable.):
```
cd verilator
git pull
git checkout verilator_3_886
```
In Verilator directory, build and install:
```
cd verilator
unset VERILATOR_ROOT
autoconf
./configure
make
sudo make install
```

### Clone lab repository
Run the following from the terminal to clone lab resources.
```
cd ~/
git clone https://github.com/zavs/ercesiMIPS.git
```

### Write your CPU and test it
A template ALU block has been presented in `src/main/scala/SingleCycle/ALU.scala` with the tester in `src/test/scala/SingleCycle/ALUtests.scala`. We borrowed the Launcher method from [ucb-bar/chisel-tutorial](https://github.com/ucb-bar/chisel-tutorial).
A new argument for shell runner needs to be name in `Launcher.scala` as the arguments of Map function:
```scala
object Launcher {
  val tests = Map(
        "ALU" -> { (backendName: String) =>
              Driver(() => new ALU(), backendName) {
                (c) => new ALUTests(c)
              }
            },
        "ALU11" -> { (backendName: String) =>
              Driver(() => new ALU11(), backendName) {
                (c) => new ALU11Tests(c)
              }
            }
        }
    )
}
```
In which, `"ALU"` is the argument for the shell runner, and `ALU()` is denoted the instance class of DUT in `ALUTests(c)`. Just add new argument for you own block class.

To test the ALU module with ALUtests, just input the following command in ercesiMIPS root directory.
```
./run-single.sh ALU
```
If you still want to monitor its verification via waveform, just run this:
```
./run-single.bak.v.sh ALU
```
And the output `.vcd` file can be monitored in `gtkwave` program which could be install in Ubuntu-like Linux:
```
sudo apt-get install gtkwave
```

To test multicycle CPU with TopTests, just input the following command in ercesiMIPS root directory.
```
./run-multi.sh Top
```
If you still want to monitor multicycle's waveform, just run this:
```
./run-multi.bak.v.sh Top
```

