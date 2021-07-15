#!/bin/sh

docker build \
  -t uiot-service-things:latest \
  -f uiot-service-things/Dockerfile \
  uiot-service-things/build/libs
