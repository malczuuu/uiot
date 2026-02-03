#!/bin/sh

docker build \
  -t "malczuuu/uiot-service-rule:${UIOT_VERSION:-snapshot}" \
  -f uiot-services/uiot-service-rule/Dockerfile \
  uiot-services/uiot-service-rule
