#!/bin/sh

docker build \
  -t malczuuu/uiot-service-rules:snapshot \
  -f uiot-service-rules/Dockerfile \
  uiot-service-rules/build/libs
