# Use an official Maven image to build the app
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /build

# Copy the pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code and build the jar
COPY src ./src
RUN mvn clean package -DskipTests

# Use a lightweight OpenJDK image
#FROM eclipse-temurin:17-jdk-alpine
FROM openjdk:17-jdk-slim
# Set working directory
WORKDIR /app

# Copy the built jar from local
COPY --from=build build/target/Job-0.0.1-SNAPSHOT.jar app.jar

# Expose port (same as server.port)
EXPOSE 10000

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
