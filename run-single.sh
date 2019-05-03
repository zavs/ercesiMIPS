#!/usr/bin/env bash

# Please run ./run-single.sh Top or ./run-single.sh ALU
# DO NOT run ./run-single.sh, which will test Top and ALU both.

# Add several blank lines
echo -e "\n\n\n\n\n\n\n\n"

sbt "test:runMain SingleCycle.Launcher $1"
