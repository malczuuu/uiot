FROM openjdk:11.0.11-jre-slim

WORKDIR /app

ENV SERVER_PORT 8080

EXPOSE 8080

COPY ./uiot-service-things-1.0.0-SNAPSHOT.jar /app/app.jar

ENTRYPOINT [ "java" ]
CMD [ "-Dfile.encoding=UTF-8", "-Duser.timezone=UTC", "-XX:+UseG1GC", "-jar", "/app/app.jar" ]
