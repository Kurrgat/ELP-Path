variable "aws_region" {
  description = "AWS region"
  default     = "us-east-1"
}

variable "vpc_cidr" {
  description = "CIDR block for the VPC"
  default     = "10.0.0.0/16"
}

variable "public_subnets" {
  description = "Public subnets"
  default     = ["10.0.1.0/24", "10.0.2.0/24"]
}

variable "private_subnets" {
  description = "Private subnets"
  default     = ["10.0.3.0/24", "10.0.4.0/24"]
}

variable "ecs_cluster_name" {
  description = "Name of the ECS cluster"
  default     = "ELP-Path-cluster"
}

variable "frontend_repo_name" {
  description = "ECR repository name for the frontend"
  default     = "angular-app"
}

variable "backend_repo_name" {
  description = "ECR repository name for the backend"
  default     = "java-app"
}

variable "frontend_task_family" {
  description = "Task family for the frontend"
  default     = "angular-frontend-task"
}

variable "backend_task_family" {
  description = "Task family for the backend"
  default     = "java-backend-task"
}

variable "frontend_cpu" {
  description = "CPU for the frontend task"
  default     = "256"
}

variable "frontend_memory" {
  description = "Memory for the frontend task"
  default     = "512"
}

variable "backend_cpu" {
  description = "CPU for the backend task"
  default     = "512"
}

variable "backend_memory" {
  description = "Memory for the backend task"
  default     = "1024"
}
