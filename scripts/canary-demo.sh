#!/usr/bin/env bash
set -euo pipefail

# Educational canary rollout simulation using Docker Compose.
# Starts a stable container and a candidate container, then prints staged traffic steps.
# This is a demonstration only; actual traffic routing requires a proxy/load balancer.
IMAGE="${1:-combined-ci-cd-experiment:local}"

echo "Starting stable version..."
docker run -d --rm --name student-api-stable -p 8080:8080 "$IMAGE" >/dev/null

cleanup() {
  docker rm -f student-api-stable student-api-canary >/dev/null 2>&1 || true
}
trap cleanup EXIT

echo "Starting canary candidate version..."
docker run -d --rm --name student-api-canary -p 8081:8080 "$IMAGE" >/dev/null

echo "Stage 1: Send a small share of test traffic to canary port 8081."
curl --retry 10 --retry-delay 2 --retry-connrefused -fsS http://localhost:8081/actuator/health
echo
echo "Stage 2: Check logs, health, and error rates before increasing traffic."
docker logs --tail 20 student-api-canary
echo "Stage 3: If healthy, route more traffic to the candidate using a real proxy/load balancer."
echo "Stage 4: If unhealthy, keep stable version and remove the canary."
echo "Note: This script demonstrates two versions on separate ports; it does not automatically split production traffic."
