#!/usr/bin/env bash

TESTER_BACKENDS=verilator sbt "test:runMain SingleCycle.Launcher $1"
