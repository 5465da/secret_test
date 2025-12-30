# AWS Secret Retriever

A simple Java application that retrieves a secret from AWS Secrets Manager and displays it.

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- AWS credentials configured (e.g., via AWS CLI or IAM roles on EC2)

## Building the Application

Run the following command to build the application:

```bash
mvn clean package
```

This will create a JAR file in the `target` directory.

## Running the Application

To run the application:

```bash
java -jar target/aws-secret-app-1.0-SNAPSHOT.jar
```

## Building and Running with Docker

Build the Docker image:

```bash
docker build -t aws-secret-app .
```

Run the container:

```bash
docker run --rm aws-secret-app
```

## Deployment on AWS EC2

1. Launch an EC2 instance with Docker installed.
2. Configure AWS credentials (e.g., IAM role with SecretsManagerReadOnly policy).
3. Build and run the Docker container as above, or push the image to ECR and pull on EC2.

## Configuration

The secret name is hardcoded as "hash_salt" and region as "ap-southeast-2". Modify the code as needed.