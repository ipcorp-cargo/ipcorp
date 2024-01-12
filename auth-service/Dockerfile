FROM openjdk:17
WORKDIR /auth-service
COPY ./target/auth-service-0.0.1-SNAPSHOT.jar .
RUN mkdir -p /tmp/git
ENTRYPOINT ["java", "-jar", "auth-service-0.0.1-SNAPSHOT.jar"]
