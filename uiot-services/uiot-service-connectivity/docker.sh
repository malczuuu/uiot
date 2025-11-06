#!/bin/sh

docker build \
  -t "malczuuu/uiot-service-connectivity:${UIOT_VERSION:-snapshot}" \
  -f uiot-services/uiot-service-connectivity/Dockerfile \
  uiot-services/uiot-service-connectivity/build/libs
