#!/bin/bash
docker run -it --rm -u 1000:1000 -v ${PWD}:/mnt maven:3-jdk-8 /bin/bash /mnt/build.sh
docker build -t registry.netopia.ca/shortbin:1.2 .
