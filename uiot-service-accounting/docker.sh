#!/bin/sh

docker build \
  -t "malczuuu/uiot-service-accounting:${UIOT_VERSION:-snapshot}" \
  -f uiot-service-accounting/Dockerfile \
  uiot-service-accounting/build/libs
