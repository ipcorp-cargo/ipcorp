FROM openjdk:17
VOLUME /app
WORKDIR /eureka-server
COPY ./target/eureka-server-0.0.1-SNAPSHOT.jar .
ENTRYPOINT ["java", "-jar", "eureka-server-0.0.1-SNAPSHOT.jar"]