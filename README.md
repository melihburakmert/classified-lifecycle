# Classifieds Lifecycle API Documentation

## Overview

This API allows users to create and manage classified advertisements, including status transitions, statistics, and history tracking. \
The API is designed as a POC, following the business rules and requirements described in the project scope.

## Remarks

Modular monolith (Modulith) architecture is used, with a focus on clean code principles and OOP practices. \
Dashboard module is dedicated to read-only operations, extensible for future features (e.g., user management). \
Classifieds module is responsible for managing classified ads lifecycle.

## Running the Application

### Prerequisites
- Java 11
- Maven 3.6+
- Docker (optional for containerization)

### Running Locally
1. Clone the repository:
   ```bash
   git clone https://github.com/melihburakmert/classified-lifecycle.git
   cd classified-lifecycle
    ```
2. Build the application:
   ```bash
   mvn clean package
   ```
   
3. Run the service:
   ```bash
    java -jar target/classified-lifecycle-0.0.1-SNAPSHOT.jar
    ```
### Running with Docker

1. Build and start the application:
   ```bash
   mvn clean package jib:dockerBuild
   cd docker
   docker-compose up -d
   ```
   
This will start the application on `localhost:8080`.

2. You can check the logs:
   ```bash
   docker-compose logs -f classified-lifecycle
   ```

### Testing the Application

1. Access the Swagger UI for API documentation and testing:
   ```
   http://localhost:8080/swagger-ui.html
   ```

## Base URL

```
http://localhost:8080
```

---

## Endpoints

### 1. Create Classified Ad

**POST /classifieds**

Create a new classified advertisement.

#### Request Body

```json
{
  "title": "string (10-50 chars, starts with letter/number, no badwords)",
  "description": "string (20-200 chars)",
  "category": "REAL_ESTATE | VEHICLE | SHOPPING | OTHER"
}
```

#### Business Rules

- Title must start with a letter or number, 10-50 characters, and not contain any word from Badwords.txt.
- Description must be 20-200 characters.
- Category must be one of: REAL_ESTATE, VEHICLE, SHOPPING, OTHER.

#### Response

- **201 Created**

```json
{
  "id": "uuid",
  "title": "string",
  "description": "string",
  "category": "string",
  "status": "PENDING_APPROVAL | ACTIVE | DUPLICATE",
  "createdAt": "date-time",
  "expiresAt": "date-time"
}
```

- **400 Bad Request**: Validation errors (e.g., badwords, invalid length).
- **Duplicate Rule**: If an ad with the same title, description, and category exists, status will be `DUPLICATE`.

---

### 2. Activate Classified Ad

**POST /classifieds/{id}/activate**

Approve and activate an ad currently in `PENDING_APPROVAL` status.

#### Path Parameter

- `id`: UUID of the classified ad.

#### Response

- **200 OK**: Returns updated ad.
- **400 Bad Request**: If ad is not in `PENDING_APPROVAL`.
- **404 Not Found**: If ad does not exist.

---

### 3. Deactivate Classified Ad

**POST /classifieds/{id}/deactivate**

Deactivate an ad currently in `ACTIVE` or `PENDING_APPROVAL` status.

#### Path Parameter

- `id`: UUID of the classified ad.

#### Response

- **200 OK**: Returns updated ad.
- **400 Bad Request**: If ad is not in `ACTIVE` or `PENDING_APPROVAL`.
- **404 Not Found**: If ad does not exist.

---

### 4. Classifieds Statistics

**GET /dashboard/classifieds/statistics**

Get statistics of all classified ads grouped by status.

#### Response

- **200 OK**

```json
{
  "countByStatus": {
    "ACTIVE": 151,
    "INACTIVE": 71,
    "PENDING_APPROVAL": 23,
    "DUPLICATE": 5,
    "EXPIRED": 12
  }
}
```

---

### 5. Classified Status History

**GET /dashboard/classifieds/{id}/history**

List all status changes for a specific classified ad.

#### Path Parameter

- `id`: UUID of the classified ad.

#### Response

- **200 OK**

```json
[
  {
    "id": "uuid",
    "classifiedId": "uuid",
    "previousStatus": "string",
    "newStatus": "string",
    "changedAt": "date-time"
  }
]
```

- **404 Not Found**: If ad does not exist.

---

## Status Lifecycle & Rules

- **Initial Status**:
  - REAL_ESTATE, VEHICLE, OTHER: `PENDING_APPROVAL`
  - SHOPPING: `ACTIVE`
- **Expiration**:
  - REAL_ESTATE: 4 weeks
  - VEHICLE: 3 weeks
  - SHOPPING, OTHER: 8 weeks
- **Duplicate Detection**: Same title, description, and category → status `DUPLICATE` (cannot be updated).
- **Activation**: Only `PENDING_APPROVAL` ads can be activated.
- **Deactivation**: Only `ACTIVE` or `PENDING_APPROVAL` ads can be deactivated.
- **No update for title, description, or category after creation.**

---

## Error Responses

All error responses follow:

```json
{
  "status": "integer",
  "message": "string",
  "details": "string (optional)",
  "timestamp": "date-time (optional)"
}
```

---

## Technologies

- Java 11, Spring Boot 2+, Maven 3.6+
- In-memory database (H2)
- Database migrations with Liquibase
- Instancio for test data generation
- OpenAPI Generator for OpenAPI/Swagger documentation

---

## Notes

- Requests taking longer than 5ms are logged.
- Unit and integration tests are included.
- Containerization via Docker/Jib.

---

## Swagger UI

Interactive API documentation is available at:

```
http://localhost:8080/swagger-ui.html
```

