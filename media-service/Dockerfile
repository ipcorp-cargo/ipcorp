FROM openjdk:17
VOLUME /app
WORKDIR /media-service
COPY ./target/media-service-0.0.1-SNAPSHOT.jar .
ENTRYPOINT ["java", "-jar", "media-service-0.0.1-SNAPSHOT.jar"]