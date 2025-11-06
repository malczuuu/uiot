#!/bin/sh

docker build \
  -t "malczuuu/uiot-service-rules:${UIOT_VERSION:-snapshot}" \
  -f uiot-services/uiot-service-rules/Dockerfile \
  uiot-services/uiot-service-rules/build/libs
