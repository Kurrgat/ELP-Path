provider "aws" {
  region = "us-east-1" # Adjust to your AWS region
}

# VPC and Networking
module "vpc" {
  source  = "terraform-aws-modules/vpc/aws"
  version = "3.19.0"

  name = "ecs-vpc"
  cidr = "10.0.0.0/16"

  azs             = ["us-east-1a", "us-east-1b"]
  public_subnets  = ["10.0.1.0/24", "10.0.2.0/24"]
  private_subnets = ["10.0.3.0/24", "10.0.4.0/24"]

  enable_nat_gateway = true
}

# Security Group
resource "aws_security_group" "ecs_sg" {
  name_prefix = "ecs-sg"
  vpc_id      = module.vpc.vpc_id

  ingress {
    description      = "Allow HTTP traffic"
    from_port        = 80
    to_port          = 80
    protocol         = "tcp"
    cidr_blocks      = ["0.0.0.0/0"]
  }

  ingress {
    description      = "Allow HTTPS traffic"
    from_port        = 443
    to_port          = 443
    protocol         = "tcp"
    cidr_blocks      = ["0.0.0.0/0"]
  }

  ingress {
    description      = "Allow backend traffic"
    from_port        = 8080
    to_port          = 8080
    protocol         = "tcp"
    cidr_blocks      = ["0.0.0.0/0"]
  }

  egress {
    description      = "Allow all outbound traffic"
    from_port        = 0
    to_port          = 0
    protocol         = "-1"
    cidr_blocks      = ["0.0.0.0/0"]
  }
}

# ECS Cluster
resource "aws_ecs_cluster" "ecs_cluster" {
  name = "ELP-Path-cluster"
}

# Task Definition for Frontend
resource "aws_ecs_task_definition" "frontend_task" {
  family                   = "angular-frontend-task"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = "256"
  memory                   = "512"

  container_definitions = jsonencode([
    {
      name      = "angular-frontend"
      image     = "<aws_account_id>.dkr.ecr.us-east-1.amazonaws.com/angular-app:latest"
      essential = true
      portMappings = [
        {
          containerPort = 80
          hostPort      = 80
        }
      ]
    }
  ])
}

# Task Definition for Backend
resource "aws_ecs_task_definition" "backend_task" {
  family                   = "java-backend-task"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = "512"
  memory                   = "1024"

  container_definitions = jsonencode([
    {
      name      = "java-backend"
      image     = "<aws_account_id>.dkr.ecr.us-east-1.amazonaws.com/java-app:latest"
      essential = true
      portMappings = [
        {
          containerPort = 8080
          hostPort      = 8080
        }
      ]
    }
  ])
}

# ECS Service for Frontend
resource "aws_ecs_service" "frontend_service" {
  name            = "angular-frontend-service"
  cluster         = aws_ecs_cluster.ecs_cluster.id
  task_definition = aws_ecs_task_definition.frontend_task.arn
  launch_type     = "FARGATE"

  network_configuration {
    subnets         = module.vpc.public_subnets
    security_groups = [aws_security_group.ecs_sg.id]
  }
}

# ECS Service for Backend
resource "aws_ecs_service" "backend_service" {
  name            = "java-backend-service"
  cluster         = aws_ecs_cluster.ecs_cluster.id
  task_definition = aws_ecs_task_definition.backend_task.arn
  launch_type     = "FARGATE"

  network_configuration {
    subnets         = module.vpc.public_subnets
    security_groups = [aws_security_group.ecs_sg.id]
  }
}
