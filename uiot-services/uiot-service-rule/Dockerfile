FROM eclipse-temurin:25-alpine

WORKDIR /app

ENV SERVER_PORT=8080
ENV LOGGING_STRUCTURED_FORMAT_CONSOLE=ecs

EXPOSE 8080

COPY ./build/libs/uiot-service-rule*.jar /app/app.jar

ENV JAVA_OPTS_DEFAULT="\
-XX:+UseContainerSupport \
-XX:MaxRAMPercentage=75.0 \
-XX:+ExitOnOutOfMemoryError \
-XX:+HeapDumpOnOutOfMemoryError \
-XX:HeapDumpPath=/tmp"

ENV JAVA_OPTS_EXTRA=""

ENV APP_ARGS=""

CMD ["/bin/sh", "-c", "exec java $JAVA_OPTS_DEFAULT $JAVA_OPTS_EXTRA -jar /app/app.jar $APP_ARGS"]
