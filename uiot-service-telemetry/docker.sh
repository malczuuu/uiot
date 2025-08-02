#!/bin/sh

docker build \
  -t malczuuu/uiot-service-telemetry:1.0.0-SNAPSHOT \
  -t malczuuu/uiot-service-telemetry:latest \
  -f uiot-service-telemetry/Dockerfile \
  uiot-service-telemetry/build/libs
