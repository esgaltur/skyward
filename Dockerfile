# Stage 1: Extract application layers
# Use the OpenJDK 17 slim image as the base image for extracting layers
FROM openjdk:17-jdk-slim AS layertools
# Set the working directory inside the container to /app
WORKDIR /app
 # Define a build-time argument for the JAR file location
ARG JAR_FILE=skyward-server/target/skyward-server-0.0.1-SNAPSHOT.jar

# Copy the already built JAR file into the container
# Copy the JAR file specified by the JAR_FILE argument to application.jar in the container
COPY ${JAR_FILE} application.jar

# Extract the layers from the JAR file using the layertools mode
RUN java -Djarmode=layertools -jar application.jar extract  # Run the jar file in layertools mode to extract the layers

# Stage 2: Build the final image
# Use the OpenJDK 17 slim image as the base image for the final image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container to /app
WORKDIR /app

# Copy extracted layers from the layertools stage into the final image
# Copy the dependencies layer from the layertools stage
COPY --from=layertools /app/dependencies/ ./
# Copy the Spring Boot loader layer from the layertools stage
COPY --from=layertools /app/spring-boot-loader/ ./
# Copy the snapshot dependencies layer from the layertools stage
COPY --from=layertools /app/snapshot-dependencies/ ./
# Copy the application layer from the layertools stage
COPY --from=layertools /app/application/ ./
# Expose port 8080 for the application
EXPOSE 8080

# Set the entry point to run the application using the JarLauncher
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
