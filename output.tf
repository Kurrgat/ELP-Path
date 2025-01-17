# Output the ECR Repository URI for the backend
output "backend_repo_uri" {
  description = "URI of the backend ECR repository"
  value       = aws_ecr_repository.backend.repository_url
}

# Output the ECS Cluster Name
output "ecs_cluster_name" {
  description = "The ECS Cluster name"
  value       = aws_ecs_cluster.ecs_cluster.name
}

# Output the ECS Service Name
output "ecs_service_name" {
  description = "The ECS Service name"
  value       = aws_ecs_service.backend_service.name
}

# Output the ECS Task Definition ARN
output "ecs_task_definition" {
  description = "The ECS Task Definition ARN"
  value       = aws_ecs_task_definition.backend_task_definition.arn
}

# Output the RDS Endpoint
output "rds_endpoint" {
  description = "The endpoint for the RDS database"
  value       = aws_db_instance.mariadb_instance.endpoint
}

# Output the RDS Database Identifier
output "rds_identifier" {
  description = "The identifier for the RDS instance"
  value       = aws_db_instance.mariadb_instance.id
}

# Output the RDS Database Name
output "rds_name" {
  description = "The database name for the RDS instance"
  value       = aws_db_instance.mariadb_instance.name
}
