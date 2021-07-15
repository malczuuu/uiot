#!/bin/sh

docker build \
  -t uiot-service-history:latest \
  -f uiot-service-history/Dockerfile \
  uiot-service-history/build/libs
