FROM openjdk:11.0.11-jre-slim

WORKDIR /app

EXPOSE 8335

COPY ./uiot-service-things-1.0.0-SNAPSHOT.jar /app/app.jar

ENTRYPOINT [ "java" ]
CMD [ "-Dfile.encoding=UTF-8", "-Duser.timezone=UTC", "-XX:+UseG1GC", "-jar", "/app/app.jar" ]
