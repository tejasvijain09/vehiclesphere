# =============================================================================
# VehicleSphere Nginx Reverse Proxy Gateway — Dockerfile
# =============================================================================

FROM nginx:1.27-alpine

LABEL maintainer="Tejasvi <tejasvijain09@gmail.com>" \
      project="VehicleSphere" \
      component="nginx-gateway"

# Copy reverse proxy configuration
COPY nginx.conf /etc/nginx/nginx.conf

EXPOSE 80

# Health check probe
HEALTHCHECK --interval=15s --timeout=5s --start-period=5s --retries=3 \
  CMD wget -qO- http://localhost/nginx-health || exit 1

CMD ["nginx", "-g", "daemon off;"]
