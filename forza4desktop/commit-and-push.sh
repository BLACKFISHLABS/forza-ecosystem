#!/usr/bin/env bash
clear
rm -rf data
rm -rf log
mvn -B verify
echo "Type your commit: "
read msg
git commit -am "$msg"
git push origin master
