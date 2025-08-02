#!/bin/sh

docker build \
  -t malczuuu/uiot-service-telemetry:snapshot \
  -f uiot-service-telemetry/Dockerfile \
  uiot-service-telemetry/build/libs
