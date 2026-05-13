# 🚀 DevMatch - Marketplace Backend

DevMatch è una piattaforma backend professionale costruita con **Spring Boot 3**, progettata per connettere freelancer e clienti. 
L'obiettivo del progetto è costruire, passo dopo passo, un backend realistico seguendo buone pratiche come architettura a layer, validazione, gestione degli errori, DTO, sicurezza, test e containerizzazione.

---

## Current Features

- User registration
- User roles: CLIENT and FREELANCER
- Job Posting CRUD
- Validation with Jakarta Bean Validation
- Global exception handling
- DTO-based request and response models
- Relationship between users and job postings
- Business rule: only CLIENT users can create job postings
  

## 🛠️ Tech Stack
  *   **Java 17+**
  *   **Spring Boot 3.x**
  *   **Spring Web**
  *   **Spring Data JPA**
  *   **Spring Validation**
  *   **H2 Database**
  *   **Lombok**
  *   **Maven**




## 🛠️ Tecnologie previste nei prossimi step:
  *   **Spring Security**
  *   **JWT Authentication**
  *   **PostgreSQL**
  *   **Docker**
  *   **JUnit 5+**
  *   **Mockito**
  *   **Swagger / OpenAPI**
    


---

  

## 🏗️ Architettura
Il progetto segue l'architettura a layer standard:
`Controller` -> `Service` -> `Repository` -> `Database`

## Project Structure

```
src/main/java/com/example/devmatch
├── controller
│   ├── UserController.java
│   └── JobPostingController.java
│
├── dto
│   ├── RegisterUserRequest.java
│   ├── UserResponse.java
│   ├── CreateJobRequest.java
│   ├── UpdateJobRequest.java
│   └── JobResponse.java
│
├── exception
│   ├── ErrorResponse.java
│   ├── GlobalExceptionHandler.java
│   └── ResourceNotFoundException.java
│
├── model
│   ├── User.java
│   ├── Role.java
│   ├── JobPosting.java
│   └── JobStatus.java
│
├── repository
│   ├── UserRepository.java
│   └── JobPostingRepository.java
│
└── service
    ├── UserService.java
    └── JobPostingService.java

```

---

## Main API Endpoints

Users

Method           	Endpoint	                                  Description

POST	             /api/users/register	                       Register a new user
GET	              /api/users                                	Get all users



Jobs

Method           	Endpoint	                                  Description

POST	             /api/jobs	                                 Create a new job posting
GET	              /api/jobs	                                 Get all job postings
GET	              /api/jobs/{id}	                            Get job posting by id
PUT              	/api/jobs/{id}	                            Update job posting
DELETE	           /api/jobs/{id}	                            Delete job posting


---

## Examples: 

# Register User

{
  "username": "client_1",
  "email": "client1@example.com",
  "password": "password123",
  "role": "CLIENT"
}

Response:

{
  "id": 1,
  "username": "client_1",
  "email": "client1@example.com",
  "role": "CLIENT"
}


# Create Job Posting

{
  "title": "Build a landing page",
  "description": "I need a responsive landing page for a SaaS product.",
  "budget": 500,
  "clientId": 1
}

Response:

{
  "id": 1,
  "title": "Build a landing page",
  "description": "I need a responsive landing page for a SaaS product.",
  "budget": 500,
  "status": "OPEN",
  "createdAt": "2026-05-12T18:09:49.2509287",
  "clientId": 1,
  "clientUsername": "client_1"
}

# Validation Error Response

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


---


## Business Rules Implemented

  *   A user can have one role: CLIENT or FREELANCER
  *   Only users with role CLIENT can create job postings
  *   A job posting must be linked to an existing client
  *   A job posting is created with default status OPEN
  *   API responses use DTOs instead of exposing JPA entities directly


## What I Did in Week 1

  *   How to structure a Spring Boot project with layers
  *   How to use Controller, Service and Repository
  *   How Dependency Injection works
  *   How to validate API requests
  *   How to handle validation errors globally
  *   How to use DTOs to separate API contracts from JPA entities
  *   How to model JPA relationships with @OneToMany and @ManyToOne
  *   How to use HTTP status codes like 200, 201, 204, 400 and 404


## Next Steps

  *   Spring Security
  *   Password hashing
  *   JWT authentication
  *   Role-based access control
  *   Unit and integration tests
  *   OpenAPI documentation



