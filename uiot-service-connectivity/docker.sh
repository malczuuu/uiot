#!/bin/sh

docker build \
  -t "malczuuu/uiot-service-connectivity:${UIOT_VERSION:-snapshot}" \
  -f uiot-service-connectivity/Dockerfile \
  uiot-service-connectivity/build/libs
