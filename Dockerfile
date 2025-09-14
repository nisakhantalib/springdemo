# Use lightweight JDK base image
FROM openjdk:21-jdk-slim

# Set app directory
WORKDIR /app

# Copy JAR from target (after mvn/gradle build)
COPY target/*.jar app.jar

# Expose port (Render provides PORT env automatically)
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
