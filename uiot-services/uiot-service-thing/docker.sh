#!/bin/sh

docker build \
  -t "malczuuu/uiot-service-thing:${UIOT_VERSION:-snapshot}" \
  -f uiot-services/uiot-service-thing/Dockerfile \
  uiot-services/uiot-service-thing
