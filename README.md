# JWT Authentication Server

![Java Version](https://img.shields.io/badge/java-21-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-green)
![JWT](https://img.shields.io/badge/JWT-Authentication-orange)
![Docker](https://img.shields.io/badge/Docker-enabled-blue)

A Simple JWT authentication server built with Spring Boot.

Available as a [Docker image](https://hub.docker.com/r/junjiewu0/jwt-auth-server).

## API Reference

| Endpoint | Method | Description | Authorization |
|----------|--------|-------------|---------------|
| `/` | GET | API info and available endpoints | Public |
| `/api/health` | GET | Service health check | Public |
| `/api/auth/register` | POST | Register a new user | Public |
| `/api/auth/login` | POST | Authenticate and receive JWT | Public |
| `/api/users/me` | GET | Get authenticated user info | JWT Required |

**Try all API endpoints with the included script:**

```bash
./scripts/try_api.sh
```

<details>
<summary>Example API Requests</summary>

### API Info

```bash
curl http://localhost:8080/
```

### Health Check

```bash
curl http://localhost:8080/api/health
```

### Register User

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "newuser",
    "email": "user@example.com",
    "password": "password123"
  }'
```

### Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "newuser",
    "password": "password123"
  }'
```

### Get Current User Info

```bash
curl -H "Authorization: Bearer JWT_TOKEN" \
  http://localhost:8080/api/users/me
```

</details>

## Development Setup

```bash
# Clone repo
git clone https://github.com/junjie-w/jwt-auth-server.git
cd jwt-auth-server

# Run with development profile
make run-dev
```

- API base URL: http://localhost:8080

## Docker Usage

### Pre-built Image from Docker Hub

```bash
# Pull image from Docker Hub
docker pull junjiewu0/jwt-auth-server

# For ARM-based machines (Apple Silicon, etc.)
docker pull --platform linux/amd64 junjiewu0/jwt-auth-server

# Run container
docker run -p 8080:8080 junjiewu0/jwt-auth-server 

# For ARM-based machines (Apple Silicon, etc.)
docker run --platform linux/amd64 -p 8080:8080 junjiewu0/jwt-auth-server
```

### Build Image Locally

```bash
# Build image
docker build -t jwt-auth-server .

# Run container
docker run -p 8080:8080 jwt-auth-server
```

## Run Tests

```bash
# Run all tests
make test

# Run a specific test class
make test-single class=UserServiceTest

# Run tests with verbose output
make test-verbose
```

## Makefile Commands

```bash
make run                           # Start the application  
make run-dev                       # Start the application with the development profile  
make run-prod                      # Start the application with the production profile  
make build                         # Clean and build the application  
make clean                         # Remove all compiled files and build artifacts  
make test                          # Run all tests  
make test-single class=            # Run a specific test class 
make test-verbose                  # Run tests with detailed output  
make docker-build                  # Build the Docker image
make docker-run                    # Run container from local image
make docker-pull-remote            # Pull pre-built image from Docker Hub
make docker-run-remote             # Run container from pre-built Docker Hub image
make try-api                       # Execute the API testing script  
```
