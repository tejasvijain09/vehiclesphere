#!/usr/bin/env bash
set -e

echo "================================================================="
echo "🌍 [IaC] VehicleSphere AWS Cloud Infrastructure (Terraform)"
echo "================================================================="

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR/../terraform"

echo "📦 1. Initializing Terraform & AWS Providers..."
terraform init -input=false

echo ""
echo "🔍 2. Validating Terraform Infrastructure Code..."
terraform validate

echo ""
echo "================================================================="
echo "✅ SUCCESS! AWS Cloud Infrastructure is 100% valid."
echo "   - Multi-AZ VPC (10.0.0.0/16) across ap-south-1a & ap-south-1b"
echo "   - Isolated RDS PostgreSQL 16 Subnets & Security Groups"
echo "   - EC2 Compute Application Cluster with Auto-Bootstrapping"
echo "   - AWS CloudWatch Alarms & S3 Storage Buckets"
echo "================================================================="
