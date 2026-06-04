# 🚀 DevMatch - Freelance Marketplace Backend

DevMatch is a backend REST API built with **Spring Boot 3** for a freelance marketplace platform.

The goal of this project is to build a realistic backend application step by step, following good practices such as layered architecture, validation, global exception handling, DTOs, authentication, authorization, testing and API documentation.

This project is part of a 30-day backend development challenge focused on improving real-world Java and Spring Boot skills.

---

## ✅ Current Features

- User registration
- User roles: `CLIENT` and `FREELANCER`
- Password hashing with BCrypt
- Login with email and password
- JWT authentication
- Role-based access control
- Job Posting CRUD
- Ownership check for job updates and deletion
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
- Improved OpenAPI documentation
  

## 🛠️ Tech Stack
  *   **Java 17+**
  *   **Spring Boot 3.x**
  *   **Spring Web**
  *   **Spring Data JPA**
  *   **Spring Security**
  *   **JWT**
  *   **BCrypt**
  *   **Jakarta Bean Validation**
  *   **H2 Database for tests/local fallback**
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

| Layer | Responsibility |
|---|---|
| Controller | Handles HTTP requests and responses |
| Service | Contains business logic |
| Repository | Handles database access |
| DTO | Defines API request/response contracts |
| Model | Represents JPA entities |
| Exception | Centralized error handling |
| Security | JWT authentication filter and security configuration |

  

## 🏗️ Architecture
The project follows the standard layer architecture:
`Controller` -> `Service` -> `Repository` -> `Database`

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
│   └── ApplicationController.java
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
│   └── ApplicationResponse.java
│
├── event
│   └── ApplicationCreatedEvent.java
│
├── exception
│   ├── ErrorResponse.java
│   ├── GlobalExceptionHandler.java
│   ├── ResourceNotFoundException.java
│   ├── InvalidCredentialsException.java
│   └── UnauthorizedActionException.java
│
├── model
│   ├── User.java
│   ├── Role.java
│   ├── JobPosting.java
│   ├── JobStatus.java
│   ├── JobApplication.java
│   └── ApplicationStatus.java
│
├── listener
│   └── ApplicationNotificationListener.java
│
├── repository
│   ├── UserRepository.java
│   ├── JobPostingRepository.java
│   └── JobApplicationRepository.java
│
├── security
│   └── JwtAuthenticationFilter.java
│
└── service
    ├── AuthService.java
    ├── JwtService.java
    ├── UserService.java
    ├── JobPostingService.java
    └── JobApplicationService.java

```

## Test Structure

```text
src/test/java/com/example/devmatch
├── integration
│   ├── UserAuthIntegrationTest.java
│   ├── JobPostingIntegrationTest.java
│   └── JobApplicationIntegrationTest.java
│
└── service
    ├── UserServiceTest.java
    ├── AuthServiceTest.java
    └── JobPostingServiceTest.java

```

---

## 🔐 Authentication & Authorization

### DevMatch uses JWT-based authentication.

#### Authentication flow:

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

#### Authorization header example:

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
| POST | `/api/jobs` | Create a new job posting | Authenticated CLIENT |
| GET | `/api/jobs?page=0&size=10&sortBy=createdAt&direction=desc` | Get paginated job postings |  Public |
| GET | `/api/jobs/{id}` | Get job posting by id | Public |
| PUT | `/api/jobs/{id}` | Update job posting | Job owner only |
| DELETE | `/api/jobs/{id}` | Delete job posting | Job owner only |

### Applications

| Method | Endpoint | Description | Access |
|---|---|---|---|
| POST | `/api/jobs/{jobId}/applications` | Apply to a job posting | Authenticated FREELANCER |
| PATCH | `/api/applications/{applicationId}/status` | Accept or reject an application | Job owner CLIENT only |


---

## 🧪 API Examples: 

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


### Login

#### Request:

##### Body:
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

### Create Job Posting
#### Requires JWT token from a CLIENT user.

#### Request:
``` text
POST /api/jobs
Authorization: Bearer <token>
```
##### Body:
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
  "clientId": 1,
  "clientUsername": "client_1"
}
```

#### Validation Error Response
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

#### Invalid Login Response
```json
{
  "timestamp": "2026-05-19T09:10:00.123456",
  "status": 401,
  "message": "Invalid email or password",
  "errors": null
}
```

#### Unauthorized Action Response
```json
{
  "timestamp": "2026-05-19T09:16:06.150169",
  "status": 403,
  "message": "Only CLIENT users can create job postings",
  "errors": null
}
```

### Apply to a Job Posting

Requires JWT token from a `FREELANCER` user.

#### Request:
``` text
POST /api/jobs/{jobId}/applications
Authorization: Bearer <token>
```
##### Body:
```json
{
  "coverLetter": "Hi, I have experience building landing pages and REST APIs."
}
```

#### Response:
```json
{
  "id": 1,
  "jobId": 1,
  "jobTitle": "Build a React landing page",
  "freelancerId": 2,
  "freelancerUsername": "freelancer_app",
  "coverLetter": "Hi, I have experience building landing pages and REST APIs.",
  "status": "PENDING",
  "createdAt": "2026-05-23T02:31:09.2767268"
}
```

### Update Application Status

Requires JWT token from the `CLIENT` owner of the job posting.

#### Request:
``` text
PATCH /api/applications/{applicationId}/status
Authorization: Bearer <token>
```
##### Body:
```json
{
  "status": "ACCEPTED"
}
```

#### Response:
```json
{
  "id": 1,
  "jobId": 1,
  "jobTitle": "Build a React landing page",
  "freelancerId": 2,
  "freelancerUsername": "freelancer_app",
  "coverLetter": "Hi, I have experience building landing pages and REST APIs.",
  "status": "ACCEPTED",
  "createdAt": "2026-05-23T02:31:09.2767268"
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

#### Integration tests use:

```text
@ActiveProfiles("test")
```


---


## 📘 Swagger/OpenAPI

### Swagger UI is available at:
```text
http://localhost:8080/swagger-ui/index.html
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
5. Paste the toke
6. Call protected endpoints


---


## 🧪 Testing

#### The project includes both unit tests and integration tests.

### Unit Tests

Unit tests focus on isolated service logic using JUnit and Mockito.

##### Covered examples:

* User registration hashes the password
* Login returns JWT when credentials are valid
* Login fails when email does not exist
* Login fails when password is incorrect
* CLIENT users can create job postings
* FREELANCER users cannot create job postings

### Integration Tests

Integration tests use `@SpringBootTest`, `@AutoConfigureMockMvc` and `MockMvc`.

##### Covered examples:

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


##### Current test result:
```text
Tests run: 37, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

##### Run tests:
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
  *   FREELANCER users cannot create job postings
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

- How to replace generic exceptions with custom business exceptions
- How to centralize domain error handling with `@RestControllerAdvice`
- How to centralize authenticated user access with a dedicated service
- How to reduce duplicated `SecurityContextHolder` logic


---


## 🚧 Next Steps

  *   Spring profiles: `dev`, `test`, `prod`
  *   Spring Boot Actuator monitoring
  *   Centralized configuration for JWT secret and expiration
  *   Improve error responses for invalid JWT tokens
  *   Add ObjectMapper-based helpers in integration tests
  *   Add unit tests for event publishing
  *   Add application status notification events
  *   Final production-style README and demo

---


## 🐳 Docker Compose

Run the full stack with PostgreSQL:

```bash
docker compose up --build
```

### Run in detached mode:

```bash
docker compose up --build -d
```

### Stop containers:

```bash
docker compose down
```

### Stop containers and remove PostgreSQL data volume:

```bash
docker compose down -v
```

### View application logs:

```bash
docker compose logs -f app
```


---

## 📊 Monitoring with Actuator

### Health endpoint:
```text
http://localhost:8080/actuator/health
```

### Health endpoint:
```text
http://localhost:8080/actuator/info
```

### Exposed endpoints:
*   health
*   info
*   metrics

### Public endpoints:
* ``` /actuator/health```
* ``` /actuator/info```


### Protected endpoints:
* ``` /actuator/metrics```


---


### Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

---


## 👨‍💻 Author

Built by Ali Alhaj Hassan as part of a 30-day Spring Boot backend development challenge.




