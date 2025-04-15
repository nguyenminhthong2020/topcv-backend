# Use a lightweight OpenJDK image
#FROM eclipse-temurin:17-jdk-alpine
FROM openjdk:17-jdk-slim
# Set working directory
WORKDIR /app

# Copy the built jar from local
COPY target/Job-0.0.1-SNAPSHOT.jar app.jar

# Expose port (same as server.port)
EXPOSE 8081

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
