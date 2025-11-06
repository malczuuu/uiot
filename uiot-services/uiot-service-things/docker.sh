#!/bin/sh

docker build \
  -t "malczuuu/uiot-service-things:${UIOT_VERSION:-snapshot}" \
  -f uiot-services/uiot-service-things/Dockerfile \
  uiot-services/uiot-service-things/build/libs
