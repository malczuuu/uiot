#!/bin/sh

docker build \
  -t uiot-service-accounting:latest \
  -f uiot-service-accounting/Dockerfile \
  uiot-service-accounting/build/libs
