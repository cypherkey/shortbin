#!/bin/bash
docker run -it --rm -v /tmp/shortbin:/mnt maven:3-jdk-8 /bin/bash /mnt/docker_build.sh
docker build -t registry.netopia.ca/shortbin:1.2 .