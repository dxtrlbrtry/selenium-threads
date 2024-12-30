FROM --platform=linux/amd64 openjdk:17-jdk-slim

# # Install system libraries
RUN apt-get update && apt-get install -y maven

# Install API project
WORKDIR /api
COPY . /api
RUN mvn package

ENTRYPOINT ["/bin/bash", "-c", "/api/mvnw spring-boot:run"]