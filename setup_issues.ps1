$repo = "ASHUVINAYAK1/astra-commerce"

function CreateIssue($title, $body, $label, $milestoneTitle) {
    gh issue create `
        --repo $repo `
        --title "$title" `
        --body "$body" `
        --label "$label" `
        --milestone "$milestoneTitle"
}

Write-Host "Creating issues with milestone titles..."

# -----------------------
# FOUNDATION
# -----------------------

CreateIssue "Setup multi module Maven parent project" @"
Description:
- Root pom.xml with packaging pom
- Add all modules
- Configure dependency management

Acceptance:
- mvn clean install works
"@ "infra" "1. Foundation and Build System"


CreateIssue "Setup User Service base structure" @"
Description:
- Spring Boot app
- Layered architecture

Acceptance:
- App runs successfully
"@ "backend" "1. Foundation and Build System"


CreateIssue "Global exception handling" @"
Description:
- ControllerAdvice
- Standard error format

Acceptance:
- Consistent error responses
"@ "backend" "1. Foundation and Build System"


# -----------------------
# CORE
# -----------------------

CreateIssue "User CRUD APIs" @"
Description:
- Entity + Repository + Service + Controller

Acceptance:
- CRUD works
"@ "backend" "2. Core Domain Services"


CreateIssue "Product Service" @"
Description:
- CRUD APIs

Acceptance:
- Product lifecycle works
"@ "backend" "2. Core Domain Services"


CreateIssue "Order Service" @"
Description:
- Order lifecycle

Acceptance:
- Orders created
"@ "backend" "2. Core Domain Services"


# -----------------------
# INFRA
# -----------------------

CreateIssue "Eureka Discovery Server" @"
Description:
- Service registry

Acceptance:
- Services registered
"@ "infra" "3. Microservice Infrastructure"


CreateIssue "Config Server" @"
Description:
- Central config

Acceptance:
- Services fetch config
"@ "infra" "3. Microservice Infrastructure"


CreateIssue "API Gateway" @"
Description:
- Routing

Acceptance:
- Gateway routes requests
"@ "infra" "3. Microservice Infrastructure"


# -----------------------
# SECURITY
# -----------------------

CreateIssue "JWT Authentication" @"
Description:
- Token auth

Acceptance:
- Secure endpoints
"@ "security" "4. Security and Access Control"


CreateIssue "Role Based Access Control" @"
Description:
- Roles

Acceptance:
- Access control works
"@ "security" "4. Security and Access Control"


# -----------------------
# EVENTS
# -----------------------

CreateIssue "Kafka Setup" @"
Description:
- Messaging system

Acceptance:
- Events flow
"@ "infra" "5. Event Driven Architecture"


CreateIssue "Order Event Flow" @"
Description:
- Async processing

Acceptance:
- Inventory updates
"@ "backend" "5. Event Driven Architecture"


# -----------------------
# DEVOPS
# -----------------------

CreateIssue "Dockerize Services" @"
Description:
- Dockerfiles

Acceptance:
- Services run in containers
"@ "devops" "6. DevOps and CI CD"


CreateIssue "CI Pipeline" @"
Description:
- GitHub Actions

Acceptance:
- Build runs on PR
"@ "devops" "6. DevOps and CI CD"


# -----------------------
# OBSERVABILITY
# -----------------------

CreateIssue "Logging System" @"
Description:
- Structured logs

Acceptance:
- Traceable logs
"@ "infra" "7. Observability and Reliability"


CreateIssue "Metrics Setup" @"
Description:
- Prometheus metrics

Acceptance:
- Metrics visible
"@ "infra" "7. Observability and Reliability"


# -----------------------
# ADVANCED
# -----------------------

CreateIssue "Circuit Breaker" @"
Description:
- Resilience4j

Acceptance:
- Failure handling
"@ "backend" "8. Advanced Engineering"


CreateIssue "Rate Limiting" @"
Description:
- API throttling

Acceptance:
- Limits enforced
"@ "infra" "8. Advanced Engineering"

Write-Host "All issues created successfully"