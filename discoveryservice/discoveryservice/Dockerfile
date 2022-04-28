FROM adoptopenjdk/openjdk11:latest
COPY target/discoveryservice-0.0.1-SNAPSHOT.jar discovery-service.jar
ENTRYPOINT ["java", "-jar", "discovery-service.jar"]