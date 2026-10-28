# Combined CI/CD Experiment (3.2.1 + 3.2.2)

**Course:** CONT_24CSP-337 — Full Stack-II  
**Experiments combined:**  
- Experiment 3.2.1: Continuous Integration (CI), automated build and testing
- Experiment 3.2.2: Continuous Deployment (CD), Docker, GitHub Actions and canary release demonstration

## Aim
To build and test a Spring Boot REST application automatically and package it as a Docker image, while demonstrating a staged canary rollout.

## Technologies
Java 17, Spring Boot, Maven, JUnit 5, MockMvc, Pact provider verification, Testcontainers, Docker, GitHub Actions.

## Application
A small Student REST API:
- `GET /api/students` — list students
- `GET /api/students/{id}` — get a student
- `POST /api/students` — create a student
- `DELETE /api/students/{id}` — delete a student
- `GET /actuator/health` — health endpoint

The sample application uses in-memory storage so it runs easily for the practical. The Testcontainers test separately starts a real PostgreSQL container to demonstrate infrastructure testing.

## Requirements
- JDK 17+
- Maven 3.9+ (or use Maven Wrapper if you add one)
- Docker Desktop / Docker Engine
- Git and a GitHub account

## Run locally
1. Extract this ZIP.
2. Open a terminal in the project directory.
3. Run tests and package the application:
   ```bash
   mvn clean verify
   ```
   Docker should be running for the PostgreSQL Testcontainers test. If Docker is unavailable, that test is configured to be skipped.
4. Start the application:
   ```bash
   mvn spring-boot:run
   ```
5. Open `http://localhost:8080/api/students` in a browser.
6. Health check: `http://localhost:8080/actuator/health`.

## Try the API
Create a student:
```bash
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{"name":"Aman","course":"CSE Data Science"}'
```
List students:
```bash
curl http://localhost:8080/api/students
```

## Build and run with Docker
Build the JAR first:
```bash
mvn clean package -DskipTests
docker build -t combined-ci-cd-experiment:local .
docker run --rm -p 8080:8080 combined-ci-cd-experiment:local
```
Alternatively:
```bash
docker compose up --build
```

## CI pipeline
The workflow `.github/workflows/ci-cd.yml` runs on pushes and pull requests to `main`:
1. Checks out source.
2. Sets up Java 17 and Maven caching.
3. Runs Maven verification tests.
4. Uploads test reports.
5. Builds a Docker image and uploads it as an artifact.

After pushing to `main`, GitHub Actions runs automatically. The workflow intentionally does not deploy to a real server because that requires a chosen hosting platform and secrets.

## Pact contract testing
The provider verification test is set up for Pact JSON files in `target/pacts`. To execute meaningful contract verification, add a consumer-generated Pact file for provider `student-service` to that folder. Without a consumer contract file, there is no real interaction contract to verify. In a larger system, a consumer test creates the Pact file and a Pact Broker can distribute it to provider CI.

## Testcontainers
`DatabaseContainerTest` starts PostgreSQL 16 in a temporary container and checks that it starts and exposes a JDBC URL. This demonstrates test infrastructure. The demo REST API itself is not wired to PostgreSQL.

## Canary deployment demonstration
Build the Docker image, then run:
```bash
chmod +x scripts/canary-demo.sh
./scripts/canary-demo.sh
```
On Windows, run the commands in the script manually in PowerShell or use Git Bash. The script starts stable and candidate versions on ports 8080 and 8081 and checks the candidate. It is a learning simulation: actual percentage-based traffic splitting requires a reverse proxy, ingress controller or load balancer.

## GitHub setup
1. Create a new empty GitHub repository.
2. Extract the ZIP and open a terminal in this folder.
3. Run:
   ```bash
   git init
   git add .
   git commit -m "Combined CI CD experiment"
   git branch -M main
   git remote add origin https://github.com/YOUR-USERNAME/YOUR-REPOSITORY.git
   git push -u origin main
   ```
4. Replace the placeholder remote URL with your repository URL.
5. Open the repository's **Actions** tab and view the workflow run.

## Procedure / algorithm
1. Implement the Spring Boot Student API.
2. Write JUnit unit tests for service logic.
3. Write MockMvc integration tests for REST endpoints.
4. Configure Pact provider verification for consumer contracts.
5. Use Testcontainers to start a temporary PostgreSQL test database.
6. Configure GitHub Actions to run tests and build the Docker image.
7. Run the packaged application in a Docker container.
8. Demonstrate stable/canary versions and describe how a load balancer would shift traffic after health checks.

## Expected outcome
- Automated build and test workflow.
- Unit and API integration tests.
- Pact provider verification scaffold.
- PostgreSQL Testcontainers demonstration.
- Dockerized Spring Boot application.
- GitHub Actions CI pipeline and canary rollout simulation.

## Result
The combined CI/CD project demonstrates automated testing, container image creation and a staged canary deployment workflow. Configure a hosting platform and secrets before treating the pipeline as production deployment.

## Viva questions
1. **What is CI?** Automatically building and testing code when changes are pushed.
2. **What is CD?** Automating delivery or deployment after the build and tests pass.
3. **Unit vs integration testing?** Unit tests validate a small component; integration tests validate multiple components working together.
4. **What is MockMvc?** A Spring test tool for testing MVC endpoints without starting a real web server.
5. **What is Pact?** A consumer-driven contract testing tool that checks agreements between services.
6. **What is Testcontainers?** A library that runs temporary dependencies such as databases in containers during tests.
7. **Why Docker?** It packages the application and runtime environment consistently.
8. **What is canary deployment?** Releasing a new version to a small amount of traffic before wider rollout.
9. **Why use GitHub Actions?** To automate repeatable build, test and delivery steps.
10. **What happens if canary fails?** Keep the stable version serving traffic and roll back the candidate.
