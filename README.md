# 📊 ClusteredData Warehouse Service

A Spring Boot application for ingesting, validating, and persisting FX deal records into a PostgreSQL database.  
Developed as part of a Scrum workflow for loading FX deal data into a data warehouse.

---

## 🚀 Features

- Accept FX deal records via REST API
- Validate:
    - Missing fields
    - Invalid data types
    - Invalid ISO currency codes
    - Duplicate deal IDs
- No rollback: valid rows are always saved
- PostgreSQL persistence
- Structured logging and proper error handling
- Dockerized (App + PostgreSQL)
- Unit tests with high coverage
- Makefile for simplified commands

---

## 🏗️ Project Structure

fx-deals-ingest/
│── src/
│ ├── main/java/...
│ ├── main/resources/
│ └── test/java/...
│── Dockerfile
│── docker-compose.yml
│── Makefile
│── pom.xml
└── README.md


---

## ⚙️ Technologies

- Java 17
- Spring Boot 3
- Spring Web
- Spring Data JPA
- PostgreSQL
- Docker & Docker Compose
- Maven
- JUnit 5 / Mockito

---

## 🔧 Running the Application

### **Prerequisites**

- Docker Desktop installed
- Maven installed (optional for local run)

---

### ▶️ Run with Docker Compose

```bash
docker-compose up --build

Application:

http://localhost:8080

PostgreSQL connection:

Host: localhost
Port: 5432
Database: fxdeals
User: fxuser
Password: fxpass

Stop containers:

docker-compose down

Reset database:

docker-compose down -v
```

### ▶️ Run Locally (without Docker)

#### Start PostgreSQL manually, then:

````
mvn spring-boot:run
````

### 📥 API Example

#### POST /api/deals

```` json
{
  "dealId": "D-10023",
  "fromCurrency": "USD",
  "toCurrency": "EUR",
  "dealTimestamp": "2025-01-01T12:30:00Z",
  "dealAmount": 1500.50
}
````

### 🧪 Running Tests

```` 
mvn test 
````

### 📦 Makefile Commands

| Command | Description |
| ----------- |:---------------------------------:|
| `make run`  | Build and start Docker containers |
| `make stop` | Stop containers |
| `make logs` | Tail logs |
| `make test` | Run unit tests |
