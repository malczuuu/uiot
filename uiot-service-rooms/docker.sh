#!/bin/sh

docker build \
  -t uiot-service-rooms:latest \
  -f uiot-service-rooms/Dockerfile \
  uiot-service-rooms/build/libs
