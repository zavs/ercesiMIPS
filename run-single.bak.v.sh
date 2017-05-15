#!/usr/bin/env bash

TESTER_BACKENDS=verilator sbt "test:run-main SingleCycle.Launcher $1"
