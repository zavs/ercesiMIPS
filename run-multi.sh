#!/usr/bin/env bash

# Please run ./run-multi.sh Top or ./run-multi.sh ALU
# DO NOT run ./run-multi.sh, which will test Top and ALU both.

# Add several blank lines
echo -e "\n\n\n\n\n\n\n\n"

sbt "test:run-main MultiCycle.Launcher $1"
