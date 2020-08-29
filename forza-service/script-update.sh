#!/bin/bash

echo "### Starting Server ###"
git pull
mvn clean install
java -jar -Xmx1536M -Duser.timezone=America/Sao_Paulo target/forza-1.0-RELEASE.jar
