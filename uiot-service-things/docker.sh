#!/bin/sh

docker build \
  -t malczuuu/uiot-service-things:snapshot \
  -f uiot-service-things/Dockerfile \
  uiot-service-things/build/libs
