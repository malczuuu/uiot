#!/bin/sh

docker build \
  -t "malczuuu/uiot-service-history:${UIOT_VERSION:-snapshot}" \
  -f uiot-services/uiot-service-history/Dockerfile \
  uiot-services/uiot-service-history
