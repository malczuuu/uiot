FROM openjdk:11.0.11-jre-slim

WORKDIR /app

EXPOSE 8334

COPY ./uiot-service-history-1.0.0-SNAPSHOT.jar /app/app.jar

ENTRYPOINT [ "java" ]
CMD [ "-Dfile.encoding=UTF-8", "-Duser.timezone=UTC", "-XX:+UseG1GC", "-jar", "/app/app.jar" ]
