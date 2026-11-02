# =============================================================================
# VehicleSphere Backend — Multi-Stage Dockerfile
# Stage 1: Build stage (Maven + Eclipse Temurin JDK 21 Alpine)
# Stage 2: Production runtime (Eclipse Temurin JRE 21 Alpine)
# =============================================================================

# --- Stage 1: Build Artifact ---
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder

WORKDIR /build

# Cache Maven dependencies layer
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build production jar
COPY src ./src
RUN mvn clean package -DskipTests -B

# --- Stage 2: Hardened Production Runtime ---
FROM eclipse-temurin:21-jre-alpine

LABEL maintainer="Tejasvi <tejasvijain09@gmail.com>" \
      project="VehicleSphere" \
      component="backend"

# Install curl for container health check probes and configure timezone
RUN apk add --no-cache curl tzdata && \
    cp /usr/share/zoneinfo/Asia/Kolkata /etc/localtime && \
    echo "Asia/Kolkata" > /etc/timezone

WORKDIR /app

# Create dedicated non-root application group and user
RUN addgroup -S vehiclesphere && adduser -S vehiclesphere -G vehiclesphere

# Copy packaged jar from builder stage
COPY --from=builder /build/target/vehiclesphere-backend-1.0.0.jar /app/app.jar

# Enforce secure file permissions
RUN chown -R vehiclesphere:vehiclesphere /app

USER vehiclesphere

EXPOSE 8080

# Production container JVM flags (respects cgroup memory & CPU limits)
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+ExitOnOutOfMemoryError -Djava.security.egd=file:/dev/./urandom"
ENV SPRING_PROFILES_ACTIVE="prod"

# Health check probe against Spring Boot Actuator
HEALTHCHECK --interval=30s --timeout=5s --start-period=35s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health/liveness || exit 1

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
