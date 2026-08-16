# SaaS API - DevOps CI/CD Project

A Spring Boot REST API project with automated testing, GitHub Actions CI, Docker containerization, Docker Compose, and automatic Docker image publishing to GitHub Container Registry (GHCR).

## Tech Stack

- Java 21
- Spring Boot
- Spring Data JPA
- Hibernate
- MySQL 8
- Maven
- JUnit 5
- Mockito
- Git & GitHub
- GitHub Actions
- Docker
- Docker Compose
- GitHub Container Registry (GHCR)

## Features

The API provides product management functionality:

- Create a product
- Get all products
- Get a product by ID
- Update a product
- Delete a product
- Search products

## Project Structure

```text
saas-api/
├── .github/
│   └── workflows/
│       ├── ci.yml
│       └── docker-publish.yml
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/devops/saas_api/
│   │   │       ├── controller/
│   │   │       ├── entity/
│   │   │       ├── repository/
│   │   │       └── service/
│   │   └── resources/
│   │
│   └── test/
│       └── java/
│           └── com/devops/saas_api/
│               └── service/
│                   └── ProductServiceTest.java
│
├── Dockerfile
├── docker-compose.yml
├── docker-compose.prod.yml
├── pom.xml
└── README.md

Testing

Unit tests are written using JUnit 5 and Mockito.

The ProductServiceTest class currently contains 7 tests.

Tests run: 7
Failures: 0
Errors: 0
Skipped: 0

All tests pass successfully in the GitHub Actions CI pipeline.

Continuous Integration

GitHub Actions is used to automatically build and test the application.

The CI workflow is triggered when code is pushed to main or when a pull request targets main.

CI Pipeline
Git Push / Pull Request
          ↓
   Checkout Repository
          ↓
       Java 21
          ↓
        Maven
          ↓
   Clean + Verify
          ↓
    Run Unit Tests

The workflow runs:

./mvnw clean verify
Dockerization

The application is containerized using Docker.

A multi-stage Dockerfile is used.

Build Stage

The build stage uses Eclipse Temurin JDK 21 to:

Copy the Maven wrapper and project files
Download Maven dependencies
Compile the application
Package the Spring Boot JAR
Runtime Stage

The runtime stage uses Eclipse Temurin JRE 21 and runs the generated JAR.

Build Stage
    ↓
Java 21 JDK
    ↓
Maven Build
    ↓
Spring Boot JAR
    ↓
Runtime Stage
    ↓
Java 21 JRE
    ↓
Running Application
Docker Compose

Docker Compose is used to run the Spring Boot application together with MySQL.

┌──────────────────────┐
│   Spring Boot App    │
│      Port 8080       │
└──────────┬───────────┘
           │
           │ Docker Network
           ↓
┌──────────────────────┐
│       MySQL 8        │
│      Database        │
└──────────────────────┘

The Spring Boot application connects to MySQL using the Docker service name:

mysql:3306

instead of localhost.

Environment Variables

Database credentials are stored using environment variables.

The application uses:

spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

The actual credentials are stored in a local .env file.

The .env file is excluded from Git using .gitignore so database credentials are not committed to the repository.

Example:

DB_USERNAME=your_username
DB_PASSWORD=your_password

Never commit actual database credentials to the repository.

Running with Docker Compose

Make sure Docker Desktop is running.

Start the application and MySQL:

docker compose up --build

The API will be available at:

http://localhost:8080

Stop the containers with:

docker compose down
Production Compose Configuration

A separate docker-compose.prod.yml is included for deployment.

Unlike the local Compose configuration, the production configuration uses the Docker image published to GitHub Container Registry:

ghcr.io/aashish1118/saas-api:latest

The production configuration also includes container health checks.

The project currently has the production Compose configuration prepared, but it is not deployed to a public cloud server.

GitHub Container Registry

GitHub Actions automatically builds and publishes the Docker image to GitHub Container Registry.

Publishing Flow
Git Push to main
       ↓
GitHub Actions
       ↓
Run Tests
       ↓
Build Docker Image
       ↓
Login to GHCR
       ↓
Push Docker Image

The published image is:

ghcr.io/aashish1118/saas-api:latest

A commit-specific image tag is also generated using the Git commit SHA.

DevOps Workflow

The complete workflow is:

                 GitHub Repository
                        │
                        ↓
                GitHub Actions
                  ┌─────┴─────┐
                  ↓           ↓
                 CI       Docker Publish
                  │           │
                  ↓           ↓
               Tests      Build Image
                  │           │
                  ↓           ↓
              Success       GHCR
                              │
                              ↓
                         Docker Image
Verification

The following components have been successfully verified:

Git repository and GitHub workflow
Pull request workflow
GitHub Actions CI
7/7 unit tests passing
Docker image build
Docker Compose
Spring Boot container
MySQL container
Docker networking
Spring Boot to MySQL connection
API response with 200 OK
Docker image publishing to GHCR
Future Improvements

Possible future improvements include:

Deploying the application to a cloud/VPS server
HTTPS and domain configuration
Reverse proxy
Automated server deployment
Monitoring and centralized logging
Kubernetes deployment

Author
Ramesh Bhandari