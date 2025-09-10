#!/bin/sh

docker build \
  -t "malczuuu/uiot-service-things:${UIOT_VERSION:-snapshot}" \
  -f uiot-service-things/Dockerfile \
  uiot-service-things/build/libs
