#!/bin/bash
docker run -it --rm -v ${PWD}:/mnt maven:3-eclipse-temurin-21 /bin/bash /mnt/build.sh
docker build -t registry.netopia.ca/shortbin:7 .
