provider "aws" {
  region = "us-east-2"
}

resource "aws_ecr_repository" "backend" {
  name = "backend-repo"
}

# ECS Cluster
resource "aws_ecs_cluster" "ecs_cluster" {
  name = "my-cluster-back"
}

# IAM Roles for ECS Execution and Task Role
# Execution Role
resource "aws_iam_role" "execution_role" {
  name = "ecs-execution-role2"
  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Effect = "Allow"
      Principal = { Service = "ecs-tasks.amazonaws.com" }
      Action = "sts:AssumeRole"
    }]
  })

  managed_policy_arns = [
    "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy",
    "arn:aws:iam::aws:policy/AmazonEC2ContainerRegistryReadOnly"
  ]
}

# Inline Policy for CloudWatch Logs
resource "aws_iam_role_policy" "execution_role_policy" {
  name   = "ecs-execution-logging-policy"
  role   = aws_iam_role.execution_role.name
  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = [
          "logs:CreateLogStream",
          "logs:PutLogEvents"
        ]
        Resource = "arn:aws:logs:log-group:/ecs/backend-task:*"
      }
    ]
  })
}

# Task Role
resource "aws_iam_role" "task_role" {
  name = "ecs-task-role2"
  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Effect = "Allow"
      Principal = { Service = "ecs-tasks.amazonaws.com" }
      Action = "sts:AssumeRole"
    }]
  })
}





resource "aws_ecs_task_definition" "backend_task_definition" {
  family                   = "backend-task"
  container_definitions    = jsonencode([{
    name      = "backend"
    image     = aws_ecr_repository.backend.repository_url
    cpu       = 256
    memory    = 512
    essential = true
    logConfiguration = {
      logDriver = "awslogs"
      options = {
        awslogs-group         = "/ecs/backend-task"
        awslogs-region        = "us-east-2" # Replace with your AWS region
        awslogs-stream-prefix = "ecs"
      }
    }
  }])
  cpu                      = 256
  memory                   = 512
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  execution_role_arn       = aws_iam_role.execution_role.arn
  task_role_arn            = aws_iam_role.task_role.arn
}

# Optional: CloudWatch log group creation
resource "aws_cloudwatch_log_group" "ecs_log_group" {
  name              = "/ecs/backend-task"
  retention_in_days = 30 # Adjust retention as needed
}


# Security Group for ECS Tasks
resource "aws_security_group" "ecs_sg" {
  name_prefix = "ecs-sg-back"
  vpc_id      = "vpc-06322973708b9d9c9" # Replace with your actual VPC ID

  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}


# ECS Service for Backend
resource "aws_ecs_service" "backend_service" {
  name            = "backend-service"
  cluster         = aws_ecs_cluster.ecs_cluster.id
  task_definition = aws_ecs_task_definition.backend_task_definition.arn
  desired_count   = 1
  launch_type     = "FARGATE"

  network_configuration {
    subnets         = ["subnet-0b265d00ac0a11eb6"] # Replace with your actual Subnet IDs
    security_groups = [aws_security_group.ecs_sg.id]
    assign_public_ip = true
  }
}
