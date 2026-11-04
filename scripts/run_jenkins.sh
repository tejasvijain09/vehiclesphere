#!/usr/bin/env bash
set -e

echo "🚀 [VehicleSphere] Initializing Jenkins CI/CD Server..."

# Ensure Docker is accessible
if ! docker info >/dev/null 2>&1; then
    echo "⚠️  Docker daemon is not running. Launching Docker Desktop..."
    open -a Docker
    echo "⏳ Waiting 15 seconds for Docker daemon to become responsive..."
    sleep 15
fi

# Check if Jenkins container is already running or exited
if [ ! "$(docker ps -q -f name=vehiclesphere-jenkins)" ]; then
    if [ "$(docker ps -aq -f status=exited -f name=vehiclesphere-jenkins)" ]; then
        echo "🔄 Resuming existing Jenkins container..."
        docker start vehiclesphere-jenkins >/dev/null
    else
        echo "📦 Starting Jenkins LTS container on port 8085..."
        docker run -d \
          --name vehiclesphere-jenkins \
          --restart unless-stopped \
          -p 8085:8080 \
          -p 50000:50000 \
          -v jenkins_home:/var/jenkins_home \
          -v /var/run/docker.sock:/var/run/docker.sock \
          jenkins/jenkins:lts-jdk21 >/dev/null
    fi
else
    echo "✅ Jenkins container is already running."
fi

echo "⏳ Checking Jenkins readiness..."
PASSWORD=""
for i in {1..30}; do
    PASSWORD=$(docker exec vehiclesphere-jenkins cat /var/jenkins_home/secrets/initialAdminPassword 2>/dev/null || true)
    if [ -n "$PASSWORD" ]; then
        break
    fi
    sleep 2
done

echo ""
echo "================================================================="
echo "🎉 Jenkins is READY!"
echo "🌐 URL: http://localhost:8085"
if [ -n "$PASSWORD" ]; then
    echo "🔑 Admin Password: $PASSWORD"
    echo -n "$PASSWORD" | pbcopy
    echo "📋 (Password automatically copied to clipboard! Just press Cmd+V)"
fi
echo "================================================================="
echo ""

# Automatically open in browser
open "http://localhost:8085"
