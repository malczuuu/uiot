#!/bin/sh

docker build \
  -t malczuuu/uiot-service-history:1.0.0-SNAPSHOT \
  -t malczuuu/uiot-service-history:latest \
  -f uiot-service-history/Dockerfile \
  uiot-service-history/build/libs
