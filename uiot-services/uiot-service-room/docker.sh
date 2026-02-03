#!/bin/sh

docker build \
  -t "malczuuu/uiot-service-room:${UIOT_VERSION:-snapshot}" \
  -f uiot-services/uiot-service-room/Dockerfile \
  uiot-services/uiot-service-room
