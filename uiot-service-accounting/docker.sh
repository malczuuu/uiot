#!/bin/sh

docker build \
  -t malczuuu/uiot-service-accounting:snapshot \
  -f uiot-service-accounting/Dockerfile \
  uiot-service-accounting/build/libs
