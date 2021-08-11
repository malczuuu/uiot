#!/bin/sh

docker build \
  -t uiot-service-rules:latest \
  -f uiot-service-rules/Dockerfile \
  uiot-service-rules/build/libs
