# 🚀 DevMatch - Freelance Marketplace Backend

DevMatch is a backend REST API built with **Spring Boot 3** for a freelance marketplace platform.

The goal of this project is to build a realistic backend application step by step, following good practices such as layered architecture, validation, global exception handling, DTOs, authentication, authorization, testing and API documentation.

This project is part of a 30-day backend development challenge focused on improving real-world Java and Spring Boot skills.


---

## 🎯 What This Project Demonstrates

This project demonstrates the ability to build a production-style backend API using Java and Spring Boot.

It covers:

- REST API design
- Layered architecture
- Authentication and authorization with JWT
- Role-based access control
- Ownership-based authorization
- Business rules modeling
- DTO-based API contracts
- Global exception handling
- Custom business exceptions
- PostgreSQL persistence
- Dockerized environment
- Spring Profiles for different environments
- Automated unit and integration testing
- API documentation with Swagger/OpenAPI
- Monitoring with Spring Boot Actuator
- Externalized configuration with environment variables

---

## 🔄 Core Business Flow

1. A user registers as either `CLIENT` or `FREELANCER`
2. A `CLIENT` creates a job posting
3. A `FREELANCER` applies to a job posting
4. The job owner reviews the application
5. The job owner accepts or rejects the application
6. If the application is accepted, the job owner can create a simulated payment
7. The payment is stored with status `COMPLETED`


---

## 🗓️ 30-Day Development Roadmap

DevMatch was built incrementally during a 30-day backend development challenge.

| Phase | Focus |
|---|---|
| Days 1-7 | Project setup, layered architecture, users, validation, exception handling, job CRUD, DTOs and relationships |
| Days 8-14 | Spring Security, password hashing, login, JWT authentication, authorization, unit tests, integration tests and Swagger |
| Days 15-21 | Pagination, filtering, job applications, application status management, Spring Events, Docker, Docker Compose and PostgreSQL |
| Days 22-30 | Auditing, Actuator, Spring Profiles, simulated payments, custom business exceptions, refactoring, Swagger JWT support, README polish and final launch |

---


## ✅ Current Features

- User registration
- User roles: `CLIENT` and `FREELANCER`
- Password hashing with BCrypt
- Login with email and password
- JWT authentication
- Role-based access control
- Ownership checks for protected resources
- Job Posting CRUD
- Pagination, sorting and filtering for job postings
- Validation with Jakarta Bean Validation
- Global exception handling
- DTO-based request and response models
- Relationship between users and job postings
- Freelancers can apply to job postings
- Duplicate applications prevention
- Application status management: `PENDING`, `ACCEPTED`, `REJECTED`
- Clients can accept or reject job applications
- Event-based notification simulation with Spring Events
- Dockerized Spring Boot application
- Docker Compose setup with PostgreSQL
- Unit tests with JUnit and Mockito
- Integration tests with MockMvc
- Swagger/OpenAPI documentation
- Automatic auditing with `createdAt` and `updatedAt`
- Application monitoring with Spring Boot Actuator
- Environment-specific configuration with Spring Profiles
- Simulated payments for accepted applications
- Custom business exceptions for domain-specific errors
- JWT authentication support in Swagger UI
- Externalized configuration with environment variables
  

## 🛠️ Tech Stack
  *   **Java 17+**
  *   **Spring Boot 3.x**
  *   **Spring Web**
  *   **Spring Data JPA**
  *   **Spring Security**
  *   **JWT**
  *   **BCrypt**
  *   **Jakarta Bean Validation**
  *   **H2 Database for dev/test**
  *   **Lombok**
  *   **Maven**
  *   **JUnit 5**
  *   **Mockito**
  *   **MockMvc**
  *   **Swagger/OpenAPI**
  *   **PostgreSQL**
  *   **Docker**
  *   **Docker Compose**
  *   **Spring Events**
  *   **SLF4J Logging**
  *   **Spring Data JPA Auditing**
  *   **Spring Boot Actuator**


---

## Main Responsibilities

| Layer             | Responsibility |
|-------------------|---|
| Controller        | Handles HTTP requests and responses |
| Service           | Contains business logic |
| Repository        | Handles database access |
| DTO               | Defines API request/response contracts |
| Model             | Represents JPA entities |
| Exception         | Centralized error handling |
| Security          | JWT authentication filter and security configuration |
| Event / Listener  |  Publishes and handles application events |
| Config            | Application, security and OpenAPI configuration |

  

## 🏗️ Architecture
The project follows the standard layer architecture:
`Controller` -> `Service` -> `Repository` -> `Database`

Main architectural decisions:

- Controllers expose REST endpoints
- Services contain business rules
- Repositories handle persistence
- DTOs separate API contracts from JPA entities
- Security is handled through JWT and Spring Security
- Domain-specific errors are handled with custom exceptions
- Events are used to decouple notification logic from business logic

---

## Project Structure

```text
src/main/java/com/example/devmatch
├── config
│   ├── SecurityConfig.java
│   └── OpenApiConfig.java
│   
├── controller
│   ├── AuthController.java
│   ├── UserController.java
│   ├── JobPostingController.java
│   ├── JobApplicationController.java
│   ├── ApplicationController.java
│   └── PaymentController.java
│
├── dto
│   ├── AuthResponse.java
│   ├── LoginRequest.java
│   ├── RegisterUserRequest.java
│   ├── UserResponse.java
│   ├── CreateJobRequest.java
│   ├── UpdateJobRequest.java
│   ├── JobResponse.java
│   ├── PagedResponse.java
│   ├── CreateApplicationRequest.java
│   ├── UpdateApplicationStatusRequest.java
│   ├── ApplicationResponse.java
│   ├── CreatePaymentRequest.java
│   └── PaymentResponse.java
│
├── event
│   └── ApplicationCreatedEvent.java
│
├── exception
│   ├── ErrorResponse.java
│   ├── GlobalExceptionHandler.java
│   ├── ResourceNotFoundException.java
│   ├── InvalidCredentialsException.java
│   ├── UnauthorizedActionException.java
│   ├── DuplicateApplicationException.java
│   ├── InvalidApplicationStatusException.java
│   ├── DuplicatePaymentException.java
│   └── InvalidPaymentException.java
│
├── listener
│   └── ApplicationNotificationListener.java
│ 
├── model
│   ├── audit
│   │   └── Auditable.java
│   │
│   ├── User.java
│   ├── Role.java
│   ├── JobPosting.java
│   ├── JobStatus.java
│   ├── JobApplication.java
│   ├── ApplicationStatus.java
│   ├── Payment.java
│   └── PaymentStatus.java
│
│
├── repository
│   ├── UserRepository.java
│   ├── JobPostingRepository.java
│   ├── JobApplicationRepository.java
│   └── PaymentRepository.java
│
├── security
│   └── JwtAuthenticationFilter.java
│
└── service
    ├── AuthService.java
    ├── JwtService.java
    ├── CurrentUserService.java
    ├── UserService.java
    ├── JobPostingService.java
    ├── JobApplicationService.java
    └── PaymentService.java

```

## Test Structure

```text
src/test/java/com/example/devmatch
├── integration
│   ├── UserAuthIntegrationTest.java
│   ├── JobPostingIntegrationTest.java
│   ├── JobApplicationIntegrationTest.java
│   ├── PaymentIntegrationTest.java
│   ├── ActuatorIntegrationTest.java
│   └── OpenApiIntegrationTest.java
│
└── service
    ├── UserServiceTest.java
    ├── AuthServiceTest.java
    └── JobPostingServiceTest.java

```

---

## 🔐 Authentication & Authorization

 DevMatch uses JWT-based authentication.

### Authentication flow:

```
1. User registers with email and password
2. Password is hashed using BCrypt
3. User logs in with email and password
4. Backend validates credentials
5. Backend generates a JWT
6. Client sends the JWT in the Authorization header
7. JwtAuthenticationFilter validates the token
8. Spring Security authenticates the request

```

### Authorization header example:

```
Authorization: Bearer <token>
```

---

## Main API Endpoints

### Auth

| Method | Endpoint | Description | Access |
|---|---|---|---|
| POST | `/api/auth/login` | Login and receive JWT token | Public |


### Users

| Method | Endpoint | Description | Access |
|---|---|---|---|
| POST | `/api/users/register` | Register a new user | Public |
| GET | `/api/users` | Get all users | Public for development |

### Jobs

| Method | Endpoint | Description | Access |
|---|---|---|---|
| POST | `/api/jobs` | Create a new job posting | Authenticated `CLIENT` |
| GET | `/api/jobs?page=0&size=10&sortBy=createdAt&direction=desc` | Get paginated job postings |  Public |
| GET | `/api/jobs/{id}` | Get job posting by id | Public |
| PUT | `/api/jobs/{id}` | Update job posting | Job owner only |
| DELETE | `/api/jobs/{id}` | Delete job posting | Job owner only |

### Applications

| Method | Endpoint | Description | Access |
|---|---|---|---|
| POST | `/api/jobs/{jobId}/applications` | Apply to a job posting | Authenticated `FREELANCER` |
| PATCH | `/api/applications/{applicationId}/status` | Accept or reject an application | Job owner `CLIENT` only |

### Payments

| Method | Endpoint | Description | Access |
|---|---|---|---|
| POST | `/api/applications/{applicationId}/payments` | Create a simulated payment for an accepted application | Job owner `CLIENT` only |

### Monitoring

| Method | Endpoint | Description | Access |
|---|---|---|---|
| GET | `/actuator/health` | Application health status | Public |
| GET | `/actuator/info` | Application info | Public |
| GET | `/actuator/metrics` | Application metrics | Protected |


---

## 🧪 API Examples

### Register User

#### Request:
```json
{
  "username": "client_1",
  "email": "client1@example.com",
  "password": "password123",
  "role": "CLIENT"
}
```

#### Response:
```json
{
  "id": 1,
  "username": "client_1",
  "email": "client1@example.com",
  "role": "CLIENT"
}
```

The password is never returned in the API response.

---

### Login

#### Request:

```json
{
  "email": "client1@example.com",
  "password": "password123"
}
```

#### Response:

```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "userId": 1,
  "username": "client_1",
  "email": "client1@example.com",
  "role": "CLIENT"
}
```

---

### Create Job Posting

 Requires JWT token from a CLIENT user.

#### Request:

``` text
POST /api/jobs
Authorization: Bearer <token>
```
#### Body:
```json
{
  "title": "Build a landing page",
  "description": "I need a responsive landing page for a SaaS product.",
  "budget": 500
}
```

#### Response:
```json
{
  "id": 1,
  "title": "Build a landing page",
  "description": "I need a responsive landing page for a SaaS product.",
  "budget": 500,
  "status": "OPEN",
  "createdAt": "2026-05-19T09:26:30.298222",
  "updatedAt": "2026-05-19T09:26:30.298222",
  "clientId": 1,
  "clientUsername": "client_1"
}
```

---

### Apply to a Job Posting

Requires JWT token from a `FREELANCER` user.

#### Request

```text
POST /api/jobs/{jobId}/applications
Authorization: Bearer <token>
```

#### Body

```json
{
  "coverLetter": "Hi, I have experience building landing pages and REST APIs."
}
```

#### Response

```json
{
  "id": 1,
  "jobId": 1,
  "jobTitle": "Build a React landing page",
  "freelancerId": 2,
  "freelancerUsername": "freelancer_app",
  "coverLetter": "Hi, I have experience building landing pages and REST APIs.",
  "status": "PENDING",
  "createdAt": "2026-05-23T02:31:09.2767268",
  "updatedAt": "2026-05-23T02:31:09.2767268"
}
```

---

### Update Application Status

Requires JWT token from the `CLIENT` owner of the job posting.

#### Request

```text
PATCH /api/applications/{applicationId}/status
Authorization: Bearer <token>
```

#### Body

```json
{
  "status": "ACCEPTED"
}
```

#### Response

```json
{
  "id": 1,
  "jobId": 1,
  "jobTitle": "Build a React landing page",
  "freelancerId": 2,
  "freelancerUsername": "freelancer_app",
  "coverLetter": "Hi, I have experience building landing pages and REST APIs.",
  "status": "ACCEPTED",
  "createdAt": "2026-05-23T02:31:09.2767268",
  "updatedAt": "2026-05-23T03:10:15.123456"
}
```

---

### Create Simulated Payment

Requires JWT token from the `CLIENT` owner of the job posting.

#### Request

```text
POST /api/applications/{applicationId}/payments
Authorization: Bearer <token>
```

#### Body

```json
{
  "amount": 800
}
```

#### Response

```json
{
  "id": 1,
  "applicationId": 1,
  "jobId": 1,
  "jobTitle": "Develop an admin dashboard",
  "clientId": 1,
  "clientUsername": "client_owner",
  "freelancerId": 3,
  "freelancerUsername": "freelancer_app",
  "amount": 800,
  "status": "COMPLETED",
  "createdAt": "2026-06-01T23:32:30.659556",
  "updatedAt": "2026-06-01T23:32:30.659556"
}
```

---

### Validation Error Response
```json
{
  "timestamp": "2026-05-12T18:10:31.3407402",
  "status": 400,
  "message": "Validation failed",
  "errors": {
    "username": "Username is mandatory",
    "email": "Email should be valid",
    "password": "Password is mandatory"
  }
}
```

---

### Invalid Login Response
```json
{
  "timestamp": "2026-05-19T09:10:00.123456",
  "status": 401,
  "message": "Invalid email or password",
  "errors": null
}
```

---

### Unauthorized Action Response
```json
{
  "timestamp": "2026-05-19T09:16:06.150169",
  "status": 403,
  "message": "Only CLIENT users can create job postings",
  "errors": null
}
```

---


## ⚙️ Spring Profiles

The project uses different Spring profiles for different environments:

| Profile | Purpose | Database |
|---|---|---|
| `dev` | Local development | H2 in-memory |
| `test` | Automated tests | H2 in-memory |
| `docker` | Docker Compose environment | PostgreSQL |


#### Default profile:

```text
dev
```

#### Docker profile is enabled through Docker Compose:

```text
SPRING_PROFILES_ACTIVE: docker
```

Integration tests use:

```text
@ActiveProfiles("test")
```


---


## 📘 Swagger/OpenAPI

### Swagger UI is available at:
```text
http://localhost:8080/swagger-ui.html
```

### OpenAPI JSON is available at:
```text
http://localhost:8080/v3/api-docs
```

Swagger UI supports JWT authentication.

### How to test protected endpoints:
1. Register a user
2. Login through /api/auth/login
3. Copy the JWT token from the response
4. Click `Authorize` in Swagger 
5. Paste the token
6. Call protected endpoints


---

## 🔐 Environment Variables

The project uses environment variables for Docker Compose and sensitive configuration such as database credentials and JWT settings.

Create a `.env` file from `.env.example`:

```bash
cp .env.example .env
```

Required variables:
```
POSTGRES_DB
POSTGRES_USER
POSTGRES_PASSWORD
SPRING_PROFILES_ACTIVE
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
JWT_SECRET
JWT_EXPIRATION
```
The `.env` file is ignored by Git and should not be committed.


---

## ▶️ Running Locally

Run the application with the default `dev` profile:

```bash
mvn spring-boot:run
```

The `dev` profile uses an in-memory H2 database.

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

H2 Console:

```text
http://localhost:8080/h2-console
```

---

## 🐳 Running with Docker Compose

Run the full stack with PostgreSQL:

```bash
docker compose up --build
```

Run in detached mode:

```bash
docker compose up --build -d
```

Stop containers:

```bash
docker compose down
```

Stop containers and remove PostgreSQL data volume:

```bash
docker compose down -v
```

View application logs:

```bash
docker compose logs -f app
```

Application:

```text
http://localhost:8080
```

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

---

## 📊 Monitoring with Actuator

Health endpoint:

```text
http://localhost:8080/actuator/health
```

Info endpoint:

```text
http://localhost:8080/actuator/info
```

Exposed endpoints:

- `health`
- `info`
- `metrics`

Public endpoints:

- `/actuator/health`
- `/actuator/info`

Protected endpoints:

- `/actuator/metrics`

---



## 🧪 Testing

The project includes both unit tests and integration tests.

### Unit Tests

Unit tests focus on isolated service logic using JUnit and Mockito.

Covered examples:

* User registration hashes the password
* Login returns JWT when credentials are valid
* Login fails when email does not exist
* Login fails when password is incorrect
* CLIENT users can create job postings
* FREELANCER users cannot create job postings

### Integration Tests

Integration tests use `@SpringBootTest`, `@AutoConfigureMockMvc` and `MockMvc`.

Covered examples:

* Register user via HTTP
* Validate bad request responses
* Login and receive JWT
* Access public job endpoints
* Create job posting with valid Bearer Token
* Block FREELANCER users from creating job postings
* Validate pagination and sorting errors
* Freelancers can apply to job postings
* Duplicate job applications are blocked
* Clients cannot apply to job postings
* Job owners can accept or reject applications
* Freelancers cannot update application status
* Non-owner clients cannot update application status
* Simulated payments can be created for accepted applications 
* Duplicate payments are blocked 
* Pending applications cannot be paid 
* Swagger/OpenAPI security scheme is exposed 
* Actuator health and info endpoints are public



Current test result:

```text
Tests run: 37, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

#### Run tests:
```bash
mvn test
```

---

## Business Rules Implemented

  *   A user can have one role: `CLIENT` or `FREELANCER`
  *   Passwords are hashed before being stored
  *   Login returns a JWT token
  *   Public endpoints can be accessed without authentication
  *   Protected endpoints require a valid JWT
  *   Only users with role `CLIENT` can create job postings
  *   `FREELANCER` users cannot create job postings
  *   A job posting is automatically linked to the authenticated client
  *   The client does not send `clientId` when creating a job
  *   Only the owner of a job posting can update it
  *   Only the owner of a job posting can delete it
  *   A job posting is created with default status `OPEN`
  *   Job postings support pagination, sorting and filtering
  *   Only `FREELANCER` users can apply to job postings
  *   `CLIENT` users cannot apply to job postings
  *   A freelancer cannot apply twice to the same job posting
  *   A new application is created with default status `PENDING`
  *   Only the job owner can update an application status
  *   `FREELANCER` users cannot update application status
  *   Application status can only be updated to `ACCEPTED` or `REJECTED`
  *   API responses use DTOs instead of exposing JPA entities directly
  *   Main entities include automatic `createdAt` and `updatedAt` audit fields
  *   Only the job owner can pay for an accepted application
  *   Only `ACCEPTED` applications can be paid
  *   A job application can only be paid once
  *   `FREELANCER` users cannot create payments


---


## 📚 What I Learned So Far

### Week 1

  *   How to structure a Spring Boot project with layers
  *   How to use Controller, Service and Repository
  *   How Dependency Injection works
  *   How to validate API requests
  *   How to handle validation errors globally
  *   How to use DTOs to separate API contracts from JPA entities
  *   How to model JPA relationships with `@OneToMany` and `@ManyToOne`
  *   How to use HTTP status codes like 200, 201, 204, 400 and 404


### Week 2

*   How to configure Spring Security
*   Difference between authentication and authorization
*   How to hash passwords with BCrypt
*   How to implement login with email and password
*   How to generate and validate JWT tokens
*   How to use Bearer Token authentication
*   How to use `SecurityContextHolder`
*   How to implement role-based access control
*   How to implement ownership checks
*   How to write unit tests with JUnit and Mockito
*   How to write integration tests with MockMvc
*   How to document APIs with Swagger/OpenAPI


### Week 3

- How to implement pagination, sorting and filtering with Spring Data JPA
- How to create generic paginated responses with `PagedResponse<T>`
- How to validate query parameters
- How to model job applications as a domain entity
- How to prevent duplicate applications with a database unique constraint
- How to manage application status transitions
- How to use Spring Events with `ApplicationEventPublisher`
- How to handle events with `@EventListener`
- How to separate notification logic from business logic
- How to dockerize a Spring Boot application
- How to create a multi-stage Dockerfile
- How to run Spring Boot and PostgreSQL with Docker Compose
- How Docker containers communicate using service names
- How to persist PostgreSQL data with Docker volumes

### Week 4

- How to add auditing with `createdAt` and `updatedAt`
- How to expose health and info endpoints with Spring Boot Actuator
- How to separate environment configuration with Spring Profiles
- How to model simulated payments as a domain resource
- How to replace generic exceptions with custom business exceptions
- How to centralize domain error handling with `@RestControllerAdvice`
- How to centralize authenticated user access with a dedicated service
- How to reduce duplicated `SecurityContextHolder` logic
- How to add JWT authentication support in Swagger UI


---


## 🚧 Future Improvements

- Improve invalid JWT error responses
- Add refresh token support
- Add ObjectMapper-based helpers in integration tests
- Add unit tests for event publishing
- Add application status notification events
- Add CI pipeline with GitHub Actions
- Add deployment configuration

---


## 👨‍💻 Author

Built by Ali Alhaj Hassan as part of a 30-day Spring Boot backend development challenge.




