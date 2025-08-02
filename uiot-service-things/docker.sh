#!/bin/sh

docker build \
  -t malczuuu/uiot-service-things:1.0.0-SNAPSHOT \
  -t malczuuu/uiot-service-things:latest \
  -f uiot-service-things/Dockerfile \
  uiot-service-things/build/libs
