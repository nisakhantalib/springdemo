# ---- Build stage ----
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

# Package the app (skip tests to speed up build)
RUN mvn clean package -DskipTests

# ---- Run stage ----
FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
