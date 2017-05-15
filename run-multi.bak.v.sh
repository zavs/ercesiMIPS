#!/usr/bin/env bash

TESTER_BACKENDS=verilator sbt "test:run-main MultiCycle.Launcher $1"
