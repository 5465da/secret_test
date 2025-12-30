# Build stage
FROM maven:3.9-eclipse-temurin-11 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:11-jre
WORKDIR /app
COPY --from=build /app/target/aws-secret-app-1.0-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]
