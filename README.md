# 🚀 DevMatch - Marketplace Backend

DevMatch è una piattaforma backend professionale costruita con **Spring Boot 3**, progettata per connettere freelancer e clienti. 
L'obiettivo del progetto è costruire, passo dopo passo, un backend realistico seguendo buone pratiche come architettura a layer, validazione, gestione degli errori, DTO, sicurezza, test e containerizzazione.

---

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

## 📈 Il Percorso dei 30 Giorni
Sto documentando l'evoluzione di questo progetto giorno dopo giorno su LinkedIn. 

<details>
  
<summary><b>Espandi per vedere la Roadmap (Giorno 1-30)</b></summary>

### Settimana 1: Foundations
- [x] **Giorno 1 — Project Setup**
        - Setup iniziale con Spring Boot 3
        - Struttura a layer: Controller, Service, Repository
        - Configurazione iniziale con JPA e database H2
        - Primo push su GitHub

- [x] **Giorno 2 — User Model & Validation**
        - Creazione della `User` entity
        - Introduzione dell’enum `Role` (`FREELANCER`, `CLIENT`)
        - Validazione dei dati con `@NotBlank`, `@Email`, `@NotNull`
        - Constructor Injection per gestire le dipendenze
        - Prime API per registrazione e lettura utenti
      
- [x] **Giorno 3 — Global Exception Handling**
        - Creazione di una risposta standard per gli errori
        - Gestione globale degli errori con `@RestControllerAdvice`
        - Gestione degli errori di validazione con `@ExceptionHandler`
        - Risposte JSON più chiare e coerenti per il client

- [x] **Giorno 4 — Job Posting CRUD**
        - Creazione della `JobPosting` entity
        - Introduzione dell’enum `JobStatus`
        - API CRUD per gli annunci di lavoro
        - Utilizzo di `ResponseEntity` e status code HTTP corretti
        - Gestione dei casi `404 Not Found`

- [x] **Giorno 5 — DTO & API Contract**
        - Introduzione dei DTO per separare Entity e API
        - Creazione di `RegisterUserRequest` e `UserResponse`
        - Creazione di `CreateJobRequest`, `UpdateJobRequest` e `JobResponse`
        - Evitata l’esposizione di campi sensibili come la password
        - Prima separazione tra modello di persistenza e contratto REST

- [ ] ... (aggiungerai gli altri man mano)
</details>



## 🚀 Come avviare il progetto
1. Clona la repository: `git clone ...`
2. Avvia con Maven: `./mvnw spring-boot:run`
3. Endpoint principale: `http://localhost:8080/api/users`
