# Stage 1: Build the application
FROM maven AS build

# Set the working directory in the container
WORKDIR /build

# Copy the pom.xml and src directory to the container
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

RUN pwd

RUN ls ./target

# Stage 2: Run the application
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /build/target/tracker-0.0.1-SNAPSHOT.jar app.jar

# Expose the port on which the application will run
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]