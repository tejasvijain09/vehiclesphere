# =============================================================================
# VehicleSphere — Terraform & AWS Provider Configuration
# =============================================================================

terraform {
  required_version = ">= 1.5.0"

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.70"
    }
    random = {
      source  = "hashicorp/random"
      version = "~> 3.6"
    }
  }

  # In team environments, configure remote state backend:
  # backend "s3" {
  #   bucket         = "vehiclesphere-terraform-state-prod"
  #   key            = "environments/prod/terraform.tfstate"
  #   region         = "ap-south-1"
  #   dynamodb_table = "vehiclesphere-terraform-locks"
  #   encrypt        = true
  # }
}

provider "aws" {
  region = var.aws_region

  default_tags {
    tags = {
      Project     = "VehicleSphere"
      Environment = var.environment
      ManagedBy   = "Terraform"
      Owner       = "DevOps-Team"
    }
  }
}
