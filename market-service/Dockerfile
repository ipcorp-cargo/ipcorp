FROM openjdk:17
VOLUME /app
WORKDIR /market-service
COPY ./target/market-service-0.0.1-SNAPSHOT.jar .
RUN mkdir -p /tmp/git
ENTRYPOINT ["java", "-jar", "market-service-0.0.1-SNAPSHOT.jar"]
