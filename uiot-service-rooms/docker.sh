#!/bin/sh

docker build \
  -t malczuuu/uiot-service-rooms:1.0.0-SNAPSHOT \
  -t malczuuu/uiot-service-rooms:latest \
  -f uiot-service-rooms/Dockerfile \
  uiot-service-rooms/build/libs
