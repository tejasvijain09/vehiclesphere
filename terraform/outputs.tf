# =============================================================================
# VehicleSphere — Terraform Outputs
# =============================================================================

output "vpc_id" {
  description = "Virtual Private Cloud Identifier"
  value       = aws_vpc.main.id
}

output "public_subnet_ids" {
  description = "Identifiers of Public Subnets"
  value       = aws_subnet.public[*].id
}

output "private_subnet_ids" {
  description = "Identifiers of Private Application Subnets"
  value       = aws_subnet.private[*].id
}

output "database_subnet_ids" {
  description = "Identifiers of Database Subnets"
  value       = aws_subnet.database[*].id
}

output "rds_endpoint" {
  description = "Primary Amazon RDS PostgreSQL Connection Endpoint"
  value       = aws_db_instance.postgres.endpoint
}

output "rds_address" {
  description = "Amazon RDS PostgreSQL Hostname"
  value       = aws_db_instance.postgres.address
}

output "rds_port" {
  description = "Amazon RDS PostgreSQL TCP Port"
  value       = aws_db_instance.postgres.port
}

output "db_secret_arn" {
  description = "AWS Secrets Manager ARN storing database master credentials"
  value       = aws_secretsmanager_secret.db_credentials.arn
}

output "app_server_public_ip" {
  description = "Public IP address of VehicleSphere Application Host"
  value       = aws_instance.app.public_ip
}

output "s3_bucket_name" {
  description = "Encrypted Amazon S3 Bucket Name for vehicle assets and invoices"
  value       = aws_s3_bucket.assets.bucket
}

output "cloudwatch_alerts_topic_arn" {
  description = "SNS Topic ARN for CloudWatch Metric Alarms"
  value       = aws_sns_topic.devops_alerts.arn
}

output "cloudwatch_dashboard_name" {
  description = "Name of CloudWatch Monitoring Dashboard"
  value       = aws_cloudwatch_dashboard.vehiclesphere.dashboard_name
}
