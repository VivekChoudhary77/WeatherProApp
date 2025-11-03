# Use Eclipse Temurin JDK 21 Alpine base image
FROM eclipse-temurin:21-jdk-alpine

# Set working directory
WORKDIR /app

# Add volume for temporary files
VOLUME /tmp

# Copy the JAR file from target directory
COPY target/weatherpro-backend-1.0.0.jar weatherpro-backend.jar

# Expose port 8080
EXPOSE 8080

# Set the entry point to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/weatherpro-backend.jar"]

# Optional: Add JVM options for better container performance
# ENTRYPOINT ["java", "-Xmx512m", "-Xms256m", "-jar", "/app/weatherpro-backend.jar"]

