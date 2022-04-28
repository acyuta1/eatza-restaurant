FROM adoptopenjdk/openjdk11:latest
COPY target/orderingservice-0.0.1-SNAPSHOT.jar order-service.jar
ENTRYPOINT ["java", "-jar", "order-service.jar"]