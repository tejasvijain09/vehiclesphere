#!/usr/bin/env bash
set -e

TARGET="${1:-frontend}"

echo "================================================================="
echo "🛡️  [DevSecOps] VehicleSphere Trivy Container Security Scanner"
echo "================================================================="

if [ "$TARGET" == "backend" ]; then
    echo "🔍 Scanning backend container image (vehiclesphere-backend:latest)..."
    docker run --rm -v /var/run/docker.sock:/var/run/docker.sock -v trivy_cache:/root/.cache/ \
      aquasec/trivy:latest image --severity HIGH,CRITICAL vehiclesphere-backend:latest
elif [ "$TARGET" == "all" ]; then
    echo "🔍 Scanning frontend container image (vehiclesphere-frontend:v2)..."
    docker run --rm -v /var/run/docker.sock:/var/run/docker.sock -v trivy_cache:/root/.cache/ \
      aquasec/trivy:latest image --severity HIGH,CRITICAL vehiclesphere-frontend:v2
    echo ""
    echo "🔍 Scanning backend container image (vehiclesphere-backend:latest)..."
    docker run --rm -v /var/run/docker.sock:/var/run/docker.sock -v trivy_cache:/root/.cache/ \
      aquasec/trivy:latest image --severity HIGH,CRITICAL vehiclesphere-backend:latest
else
    echo "🔍 Scanning frontend container image (vehiclesphere-frontend:v2)..."
    docker run --rm -v /var/run/docker.sock:/var/run/docker.sock -v trivy_cache:/root/.cache/ \
      aquasec/trivy:latest image --severity HIGH,CRITICAL vehiclesphere-frontend:v2
fi

echo ""
echo "✅ Security vulnerability scan completed."
