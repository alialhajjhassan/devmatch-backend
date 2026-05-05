# 🚀 DevMatch - Marketplace Backend

DevMatch è una piattaforma backend professionale costruita con **Spring Boot 3**, progettata per connettere freelancer e clienti. Questo progetto segue un percorso di sviluppo di 30 giorni focalizzato sulle best practices dell'ingegneria del software.

## 🛠️ Tech Stack
*   **Java 17+**
*   **Spring Boot 3.x** (Web, Data JPA, Security, Validation)
*   **Database:** H2 (Dev) / PostgreSQL (Prod)
*   **Tools:** Lombok, Docker, JUnit 5, Mockito

## 🏗️ Architettura
Il progetto segue l'architettura a layer standard:
`Controller` -> `Service` -> `Repository` -> `Database`

## 📈 Il Percorso dei 30 Giorni
Sto documentando l'evoluzione di questo progetto giorno dopo giorno su LinkedIn. 

<details>
<summary><b>Espandi per vedere la Roadmap (Giorno 1-30)</b></summary>

### Settimana 1: Foundations
- [x] **Giorno 1-2:** Project Setup & User Entity (IoC & DI basics)
- [ ] ... (aggiungerai gli altri man mano)
</details>

## 🚀 Come avviare il progetto
1. Clona la repository: `git clone ...`
2. Avvia con Maven: `./mvnw spring-boot:run`
3. Endpoint principale: `http://localhost:8080/api/users`
