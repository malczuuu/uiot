#!/bin/sh

docker build \
  -t "malczuuu/uiot-service-telemetry:${UIOT_VERSION:-snapshot}" \
  -f uiot-service-telemetry/Dockerfile \
  uiot-service-telemetry/build/libs
