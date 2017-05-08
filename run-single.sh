#!/usr/bin/env bash

# Add several blank lines
echo -e "\n\n\n\n\n\n\n\n"

sbt "test:run-main SingleCycle.Launcher $1"
