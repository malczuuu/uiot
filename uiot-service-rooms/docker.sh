#!/bin/sh

docker build \
  -t "malczuuu/uiot-service-rooms:${UIOT_VERSION:-snapshot}" \
  -f uiot-service-rooms/Dockerfile \
  uiot-service-rooms/build/libs
