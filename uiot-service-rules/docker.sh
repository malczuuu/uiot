#!/bin/sh

docker build \
  -t malczuuu/uiot-service-rules:1.0.0-SNAPSHOT \
  -t malczuuu/uiot-service-rules:latest \
  -f uiot-service-rules/Dockerfile \
  uiot-service-rules/build/libs
