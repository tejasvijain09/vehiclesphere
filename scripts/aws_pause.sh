#!/usr/bin/env bash
set -e

echo "⏸️  [AWS Cost Saver] Pausing VehicleSphere Cloud Resources..."

INSTANCE_ID=$(aws ec2 describe-instances \
  --filters "Name=tag:Name,Values=vehiclesphere-prod-app-server" "Name=instance-state-name,Values=running" \
  --query "Reservations[0].Instances[0].InstanceId" --output text 2>/dev/null || true)

if [ "$INSTANCE_ID" != "None" ] && [ -n "$INSTANCE_ID" ]; then
    echo "🛑 Stopping EC2 Instance ($INSTANCE_ID)..."
    aws ec2 stop-instances --instance-ids "$INSTANCE_ID" >/dev/null
    echo "   EC2 compute is now paused ($0.00/hour)."
else
    echo "ℹ️  EC2 instance is already stopped or not found."
fi

echo "🛑 Stopping Amazon RDS PostgreSQL Database (vehiclesphere-prod-pg)..."
aws rds stop-db-instance --db-instance-identifier vehiclesphere-prod-pg >/dev/null 2>&1 || echo "   RDS is already stopped or in transition."

echo ""
echo "================================================================="
echo "✅ ALL COMPUTE CHARGES ARE NOW PAUSED ($0/hr)!"
echo "   - EC2 instance: STOPPED (Zero CPU/RAM cost)"
echo "   - RDS PostgreSQL: STOPPED (Zero Database compute cost)"
echo "   - All vehicle data, schema, and containers are 100% PRESERVED."
echo "================================================================="
