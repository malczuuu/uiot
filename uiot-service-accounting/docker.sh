#!/bin/sh

docker build \
  -t malczuuu/uiot-service-accounting:1.0.0-SNAPSHOT \
  -t malczuuu/uiot-service-accounting:latest \
  -f uiot-service-accounting/Dockerfile \
  uiot-service-accounting/build/libs
