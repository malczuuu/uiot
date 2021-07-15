#!/bin/sh

docker build \
  -t uiot-service-connectivity:latest \
  -f uiot-service-connectivity/Dockerfile \
  uiot-service-connectivity/build/libs
