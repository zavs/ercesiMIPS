About The ercesiMIPS Processor Porject
====================================

Author: Meng Zhang (zhangm@nwpu.edu.cn)

Author: Jianfeng An (anjf@nwpu.edu.cn)

Author: Danghui Wang (wangdh@nwpu.edu.cn)

Date: 2017 April 9

Diagrams: [ercesiMIPS wiki](http://www.ercesi.org)

This repo has been put together to demonstrate a number of simple MIPS Processors written in [Chisel](http://chisel.eecs.berkeley.edu).

## Install Chisel (according to [usb-bar chisel3](https://github.com/ucb-bar/chisel3))
### [Install sbt](http://www.scala-sbt.org/release/docs/Installing-sbt-on-Linux.html)
Run the following from the terminal to install sbt.
```
echo "deb https://dl.bintray.com/sbt/debian /" | sudo tee -a /etc/apt/sources.list.d/sbt.list
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 2EE0EA64E40A89B84B2DF73499E82A75642AC823
sudo apt-get update
sudo apt-get install sbt
```


