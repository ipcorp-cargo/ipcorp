FROM openjdk:17
VOLUME /app
WORKDIR /cloud-config
COPY ./target/cloud-config-0.0.1-SNAPSHOT.jar .
ENTRYPOINT ["java", "-jar", "cloud-config-0.0.1-SNAPSHOT.jar"]