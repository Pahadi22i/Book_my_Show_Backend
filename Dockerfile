# Stage 1: Build the application (Naya JAR banayenge)
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run the application (Bana hua JAR chalayenge)
FROM eclipse-temurin:21-jdk
WORKDIR /app
# Yahan hum naya bana hua JAR copy kar rahe hain
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]