#!/bin/sh

docker build \
  -t malczuuu/uiot-service-history:snapshot \
  -f uiot-service-history/Dockerfile \
  uiot-service-history/build/libs
