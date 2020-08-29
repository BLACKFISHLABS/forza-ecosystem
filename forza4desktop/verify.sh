#!/usr/bin/env bash
clear
rm -rf data
rm -rf log
mvn -B verify
