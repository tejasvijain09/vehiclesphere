# =============================================================================
# VehicleSphere — Terraform Input Variables
# =============================================================================

variable "aws_region" {
  description = "AWS deployment region (default: Mumbai, India)"
  type        = string
  default     = "ap-south-1"
}

variable "environment" {
  description = "Deployment environment name (dev, staging, prod)"
  type        = string
  default     = "prod"
}

variable "project_name" {
  description = "Project namespace used for resource naming"
  type        = string
  default     = "vehiclesphere"
}

variable "vpc_cidr" {
  description = "Primary CIDR block for the VehicleSphere Virtual Private Cloud"
  type        = string
  default     = "10.0.0.0/16"
}

variable "availability_zones" {
  description = "Target AWS availability zones in the selected region"
  type        = list(string)
  default     = ["ap-south-1a", "ap-south-1b"]
}

variable "public_subnet_cidrs" {
  description = "CIDR blocks for public edge subnets (ALB, NAT Gateway, Bastion)"
  type        = list(string)
  default     = ["10.0.1.0/24", "10.0.2.0/24"]
}

variable "private_subnet_cidrs" {
  description = "CIDR blocks for private application subnets (EC2, ECS, EKS)"
  type        = list(string)
  default     = ["10.0.10.0/24", "10.0.11.0/24"]
}

variable "database_subnet_cidrs" {
  description = "CIDR blocks for isolated private database subnets (Amazon RDS)"
  type        = list(string)
  default     = ["10.0.20.0/24", "10.0.21.0/24"]
}

variable "instance_type" {
  description = "EC2 compute instance type for application hosting"
  type        = string
  default     = "t3.medium"
}

variable "db_instance_class" {
  description = "RDS PostgreSQL compute instance tier"
  type        = string
  default     = "db.t4g.micro"
}

variable "db_allocated_storage" {
  description = "Initial allocated storage in GB for RDS PostgreSQL"
  type        = number
  default     = 20
}

variable "db_name" {
  description = "PostgreSQL default database catalog name"
  type        = string
  default     = "vehiclesphere"
}

variable "db_username" {
  description = "PostgreSQL administrator username"
  type        = string
  default     = "vehiclesphere_admin"
}

variable "ssh_key_name" {
  description = "Optional AWS EC2 Key Pair name for SSH administration"
  type        = string
  default     = ""
}
