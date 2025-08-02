#!/bin/sh

docker build \
  -t malczuuu/uiot-service-connectivity:1.0.0-SNAPSHOT \
  -t malczuuu/uiot-service-connectivity:latest \
  -f uiot-service-connectivity/Dockerfile \
  uiot-service-connectivity/build/libs
