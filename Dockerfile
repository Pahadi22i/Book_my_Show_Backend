# Use an official OpenJDK 21 image (Lightweight Slim version)
FROM eclipse-temurin:21-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy the built jar file into the container
# This assumes you have run 'mvn clean package' before building the docker image
COPY target/book_my_show-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your application runs on (from your application.properties)
EXPOSE 8081

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]