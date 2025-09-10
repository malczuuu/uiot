#!/bin/sh

docker build \
  -t "malczuuu/uiot-service-rules:${UIOT_VERSION:-snapshot}" \
  -f uiot-service-rules/Dockerfile \
  uiot-service-rules/build/libs
