# =============================================================================
# VehicleSphere Frontend — Multi-Stage Dockerfile
# Stage 1: Build stage (Node.js 22 Alpine)
# Stage 2: Production runtime (Nginx 1.27 Alpine)
# =============================================================================

# --- Stage 1: Build Web Assets ---
FROM node:22-alpine AS builder

WORKDIR /app

# Cache package dependencies layer
COPY package.json package-lock.json* ./
RUN npm install --frozen-lockfile || npm install

# Copy application source code
COPY . .

# Accept optional build argument for API URL
ARG VITE_API_URL=/api/v1
ENV VITE_API_URL=${VITE_API_URL}

# Compile optimized static bundle
RUN npm run build

# --- Stage 2: Production Web Server ---
FROM nginx:1.27-alpine

LABEL maintainer="Tejasvi <tejasvijain09@gmail.com>" \
      project="VehicleSphere" \
      component="frontend"

# Remove default Nginx welcome site
RUN rm -rf /usr/share/nginx/html/*

# Copy custom Nginx configuration
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copy compiled static assets from builder stage
COPY --from=builder /app/dist /usr/share/nginx/html

EXPOSE 80

# Health check probe
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
  CMD wget -qO- http://127.0.0.1/healthz || exit 1

CMD ["nginx", "-g", "daemon off;"]
