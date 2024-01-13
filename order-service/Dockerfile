FROM openjdk:17
VOLUME /app
WORKDIR /order-service
COPY ./target/order-service-0.0.1-SNAPSHOT.jar .
ENTRYPOINT ["java", "-jar", "order-service-0.0.1-SNAPSHOT.jar"]