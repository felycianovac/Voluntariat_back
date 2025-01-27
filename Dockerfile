FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/*.jar app.jar

COPY src/main/resources/keystore.p12 /app/resources/keystore.p12


EXPOSE 8443

ENTRYPOINT ["java", "-jar", "app.jar"]
