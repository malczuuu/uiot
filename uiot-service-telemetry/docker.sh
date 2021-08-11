#!/bin/sh

docker build \
  -t uiot-service-telemetry:latest \
  -f uiot-service-telemetry/Dockerfile \
  uiot-service-telemetry/build/libs
