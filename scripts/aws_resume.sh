#!/usr/bin/env bash
set -e

echo "▶️  [AWS] Resuming VehicleSphere Cloud Resources for Presentation..."

echo "🚀 1. Starting Amazon RDS PostgreSQL Database (vehiclesphere-prod-pg)..."
aws rds start-db-instance --db-instance-identifier vehiclesphere-prod-pg >/dev/null 2>&1 || true

INSTANCE_ID=$(aws ec2 describe-instances \
  --filters "Name=tag:Name,Values=vehiclesphere-prod-app-server" \
  --query "Reservations[0].Instances[0].InstanceId" --output text 2>/dev/null || true)

if [ "$INSTANCE_ID" != "None" ] && [ -n "$INSTANCE_ID" ]; then
    echo "🚀 2. Starting EC2 Application Server ($INSTANCE_ID)..."
    aws ec2 start-instances --instance-ids "$INSTANCE_ID" >/dev/null 2>&1 || true
    echo "⏳ Waiting 15 seconds for instance to initialize and assign public IP..."
    aws ec2 wait instance-running --instance-ids "$INSTANCE_ID"
    NEW_IP=$(aws ec2 describe-instances --instance-ids "$INSTANCE_ID" --query "Reservations[0].Instances[0].PublicIpAddress" --output text)
    echo ""
    echo "================================================================="
    echo "🎉 Application is BACK ONLINE!"
    echo "🌐 Live Public URL: http://$NEW_IP"
    echo "================================================================="
else
    echo "❌ EC2 instance not found."
fi
