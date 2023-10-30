FROM openjdk:17.0.1-jdk-slim

WORKDIR /app

COPY build/libs/fxservice-0.0.1-SNAPSHOT.jar ./app.jar

CMD ["java", "-jar", "app.jar"]