#!/bin/sh

docker build \
  -t "malczuuu/uiot-service-rooms:${UIOT_VERSION:-snapshot}" \
  -f uiot-services/uiot-service-rooms/Dockerfile \
  uiot-services/uiot-service-rooms
