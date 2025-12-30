FROM maven:3.8.6-openjdk-11

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

CMD ["java", "-jar", "target/aws-secret-app-1.0-SNAPSHOT.jar"]