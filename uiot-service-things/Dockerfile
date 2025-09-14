FROM eclipse-temurin:17-alpine

WORKDIR /app

ENV SERVER_PORT=8080
ENV LOGGING_STRUCTURED_FORMAT_CONSOLE=ecs

EXPOSE 8080

COPY ./uiot-service-things-*.jar /app/app.jar

ENV JAVA_OPTS_DEFAULT="\
-XX:+UseContainerSupport \
-XX:MaxRAMPercentage=75.0 \
-XX:+ExitOnOutOfMemoryError \
-Dfile.encoding=UTF-8 \
-Duser.timezone=UTC \
-XX:+HeapDumpOnOutOfMemoryError \
-XX:HeapDumpPath=/tmp"

ENV EXTRA_JAVA_OPTS=""

ENV SPRING_PROFILES_ACTIVE="production"

ENV APP_ARGS=""

ENV JAVA_OPTS="$JAVA_OPTS_DEFAULT $EXTRA_JAVA_OPTS"

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar $APP_ARGS"]
