ercesiPROC处理器项目与实验计划说明
====================================

作者：张萌   (zhangm@nwpu.edu.cn)

作者：安建峰 (anjf@nwpu.edu.cn)

作者：王党辉 (wangdh@nwpu.edu.cn)

日期：／4/20／2017



该项目是配合ercesi实验室体系结构课程的实验教学内容。该项目计划使用[MIPS]()及[RISC-V](http://riscv.org)实现不同的RISC处理器。本项目分做两个象限阶段，第一阶段 ercesiMIPS，完成基本性能的MIPS指令处理器，用于计算机学院本科[“计算机组成与体系结构”，CS U10M11007](http://www.ercesi.org/courses/U10M11007/index.html)的课程教学演示实验与高级试点实验班的实验教学工作。

第二阶段 ercesiRISV-V，完成具有不同性能特征的支持RISC-V指令集的处理器，支持FPGA、ASIC等不同类型的实现机制。用于改革后的[“计算机组成与体系结构”，CS U10M11007](http://www.ercesi.org/courses/U10M11007/index.html)教学与实验需求。同时支持计算机学院研究生“Advanced Computer Architecture”，“VLSI Circuits and Systems Design”以及“Parallel Program Design for Stream-Computing”（CS M10M22014). 更进一步为[ERCESI LAB](http://www.ercesi.org)的体系结构研究提供支持和研究支撑。 

目前该项目仍然处于第一象限，该阶段工作分为5个不同的等级，根据教学内容的展开以及学生实验的完成度进行不同层次的推进。该阶段的处理器结构设计的目的是通过具体的项目设计理解基本的处理器结构，并为后续的阶段展开提供工具链支持。因此，本阶段的设计实验工作中，工具链提供两种不同的方式支持。首先推荐使用[Chisel](http://chisel.eecs.berkeley.edu)完成所有的结构描述和验证工作，通过调用Vivado或Synopsys Design Compiler以及IC Compiler等工具实现基于FPGA和ASIC底层的综合与布局布线。同时，也可以使用基于VerilogHDL的直接电路描述方式构建工程，并通过调用Vivado或Synopsys Design Compiler以及IC Compiler等工具实现基于FPGA和ASIC底层的综合与布局布线。

* 第一级 “我的第一个处理器” （完成简单的7-9(11)指令的单周期处理器设计）预计/5/4/2017完成
* 第二级 “多周期处理器”   （完成MIPS32指令集支持的多周期处理器设计，可选FPGA的实现，性能的评价）预计/5/11/2017完成
* 第三级 “流水线处理器I”   （完成流水线结构支持的MIPS结构，可选：FPGA的实现，性能评价）预计/6/15/2017完成
* 第四级 “流水线处理器II” （根据现有生态（Ported Linux、MIPS gcc compiler）完成具有流水线支持的MIPS结构的性能优化，完成FPGA实现，可选：通用测试程序集性能评价）
* 第五级 “流水线处理器III”   （可选：超标量处理器）可选内容。

## 第一级 “我的第一个处理器”
本实验推荐使用Chisel做为设计语言。本次实验已经提供了基本的结构描述以及测试描述Chisel模版，模版以一个简单ALU为例。请大家按照教学内容完成时实现对应指令的ALU结构，并设计单周期处理器的其他部分，可以将对应模块的测试程序放置在/src/test/scala/SingleCycle目录中。对应package name可根据设定实现。

测试文件的关联是通过Launcher.scala实现的。
```scala
"ALU" -> { (backendName: String) =>
      Driver(() => new ALU(), backendName) {
        (c) => new ALUTests(c)
      }
    },
```
其中ALU是运行名称，建议与模块类ALU()名称一致。

测试运行使用
    `./run-single.sh ALU`
即可执行ALU的测试文件。