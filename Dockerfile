# Use OpenJDK 17 as base image
#FROM openjdk:17-jdk-slim
FROM gcr.io/distroless/java17-debian12

# Set the working directory
WORKDIR /app

# Copy the JAR file into the container
COPY target/BatchProcessorExample-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port (change if necessary)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
