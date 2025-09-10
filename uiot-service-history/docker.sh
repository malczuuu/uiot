#!/bin/sh

docker build \
  -t "malczuuu/uiot-service-history:${UIOT_VERSION:-snapshot}" \
  -f uiot-service-history/Dockerfile \
  uiot-service-history/build/libs
