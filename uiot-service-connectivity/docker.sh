#!/bin/sh

docker build \
  -t malczuuu/uiot-service-connectivity:snapshot \
  -f uiot-service-connectivity/Dockerfile \
  uiot-service-connectivity/build/libs
