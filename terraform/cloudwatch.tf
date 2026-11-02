# =============================================================================
# VehicleSphere — AWS CloudWatch Alarms & Monitoring Dashboard
# =============================================================================

# 1. SNS Topic for DevOps Incident & Metric Alerts
resource "aws_sns_topic" "devops_alerts" {
  name              = "${var.project_name}-${var.environment}-alerts"
  kms_master_key_id = "alias/aws/sns"

  tags = {
    Name        = "${var.project_name}-alerts-topic"
    Environment = var.environment
  }
}

# 2. CloudWatch Metric Alarm: EC2 High CPU Utilization (> 80%)
resource "aws_cloudwatch_metric_alarm" "ec2_cpu_high" {
  alarm_name          = "${var.project_name}-${var.environment}-ec2-high-cpu"
  comparison_operator = "GreaterThanOrEqualToThreshold"
  evaluation_periods  = 2
  metric_name         = "CPUUtilization"
  namespace           = "AWS/EC2"
  period              = 300
  statistic           = "Average"
  threshold           = 80
  alarm_description   = "Triggered when EC2 application instance CPU exceeds 80% for two consecutive 5-minute intervals."
  alarm_actions       = [aws_sns_topic.devops_alerts.arn]
  ok_actions          = [aws_sns_topic.devops_alerts.arn]

  dimensions = {
    InstanceId = aws_instance.app.id
  }

  tags = {
    Name        = "${var.project_name}-ec2-cpu-alarm"
    Environment = var.environment
  }
}

# 3. CloudWatch Metric Alarm: RDS PostgreSQL High CPU Utilization (> 80%)
resource "aws_cloudwatch_metric_alarm" "rds_cpu_high" {
  alarm_name          = "${var.project_name}-${var.environment}-rds-high-cpu"
  comparison_operator = "GreaterThanOrEqualToThreshold"
  evaluation_periods  = 2
  metric_name         = "CPUUtilization"
  namespace           = "AWS/RDS"
  period              = 300
  statistic           = "Average"
  threshold           = 80
  alarm_description   = "Triggered when RDS PostgreSQL CPU utilization exceeds 80%."
  alarm_actions       = [aws_sns_topic.devops_alerts.arn]

  dimensions = {
    DBInstanceIdentifier = aws_db_instance.postgres.identifier
  }

  tags = {
    Name        = "${var.project_name}-rds-cpu-alarm"
    Environment = var.environment
  }
}

# 4. CloudWatch Metric Alarm: RDS Low Free Storage Space (< 5GB)
resource "aws_cloudwatch_metric_alarm" "rds_low_storage" {
  alarm_name          = "${var.project_name}-${var.environment}-rds-low-storage"
  comparison_operator = "LessThanOrEqualToThreshold"
  evaluation_periods  = 1
  metric_name         = "FreeStorageSpace"
  namespace           = "AWS/RDS"
  period              = 300
  statistic           = "Average"
  threshold           = 5368709120 # 5 GB in bytes
  alarm_description   = "Triggered when RDS PostgreSQL free disk storage drops below 5 GB."
  alarm_actions       = [aws_sns_topic.devops_alerts.arn]

  dimensions = {
    DBInstanceIdentifier = aws_db_instance.postgres.identifier
  }

  tags = {
    Name        = "${var.project_name}-rds-storage-alarm"
    Environment = var.environment
  }
}

# 5. CloudWatch Executive Observability Dashboard
resource "aws_cloudwatch_dashboard" "vehiclesphere" {
  dashboard_name = "${var.project_name}-${var.environment}-overview"

  dashboard_body = jsonencode({
    widgets = [
      {
        type   = "metric"
        x      = 0
        y      = 0
        width  = 12
        height = 6
        properties = {
          metrics = [
            ["AWS/EC2", "CPUUtilization", "InstanceId", aws_instance.app.id, { "label" : "EC2 Compute CPU %" }]
          ]
          period = 300
          stat   = "Average"
          region = var.aws_region
          title  = "VehicleSphere EC2 Host CPU Utilization"
          yAxis = {
            left = { min = 0, max = 100 }
          }
        }
      },
      {
        type   = "metric"
        x      = 12
        y      = 0
        width  = 12
        height = 6
        properties = {
          metrics = [
            ["AWS/RDS", "CPUUtilization", "DBInstanceIdentifier", aws_db_instance.postgres.identifier, { "label" : "RDS PostgreSQL CPU %" }],
            ["AWS/RDS", "DatabaseConnections", "DBInstanceIdentifier", aws_db_instance.postgres.identifier, { "label" : "Active DB Connections", "yAxis" : "right" }]
          ]
          period = 300
          stat   = "Average"
          region = var.aws_region
          title  = "Amazon RDS PostgreSQL CPU & Connections"
        }
      }
    ]
  })
}
