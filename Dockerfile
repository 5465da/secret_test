# Stage 1: Build
FROM maven:3.8.6-openjdk-11 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=build /app/target/aws-secret-app-1.0-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]
