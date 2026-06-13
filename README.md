# SwiftPay - Event Driven Payment Processing Platform

## Overview

SwiftPay is a distributed payment processing platform built using a microservices architecture.

The system processes payment requests asynchronously using Apache Kafka, maintains transaction records, validates account balances, and stores ledger entries for completed transactions.

The platform demonstrates modern backend engineering practices including:

- Microservices Architecture
- Event Driven Communication
- Kafka Messaging
- PostgreSQL Persistence
- Redis Caching
- Docker Containerization
- CI/CD using GitHub Actions
- API Documentation using Swagger
- Unit Testing with JUnit 5 and Mockito
- Code Coverage using JaCoCo

---

## Problem Statement

Traditional payment systems often suffer from:

- Tight coupling between services
- Poor scalability
- Limited fault tolerance
- Difficulty handling asynchronous operations

SwiftPay solves these problems using event-driven architecture and independent microservices.

---

## System Architecture

### Components

### Transaction Gateway Service

Responsibilities:

- Accept payment requests
- Validate sender account
- Validate available balance
- Generate transaction record
- Ensure idempotency using Redis
- Publish payment event to Kafka

Port:

```text
8080
```

---

### Ledger Service

Responsibilities:

- Consume payment events from Kafka
- Validate sender and receiver accounts
- Create ledger entries
- Update account balances
- Publish payment result event

Port:

```text
8081
```

---

### PostgreSQL

Stores:

- Accounts
- Transactions
- Ledger Entries

Port:

```text
5432
```

---

### Redis

Used for:

- Idempotency Key Validation
- Duplicate Payment Prevention

Port:

```text
6379
```

---

### Apache Kafka

Used for asynchronous communication between services.

Topics:

```text
payment-events
payment-result-events
```

Port:

```text
9092
```

---

## Event Flow

1. Client submits payment request.

2. Transaction Gateway:

   - Validates request
   - Checks sender balance
   - Saves transaction as PENDING
   - Publishes PaymentEvent

3. Kafka receives event.

4. Ledger Service consumes event.

5. Ledger Service:

   - Updates balances
   - Creates ledger entry
   - Publishes PaymentResultEvent

6. Transaction Gateway consumes result event.

7. Transaction status updated to:

```text
SUCCESS
```

or

```text
FAILED
```

---

## Architecture Diagram

```text
                     ┌─────────────────┐
                     │     Client      │
                     └────────┬────────┘
                              │
                              ▼
                 ┌─────────────────────────┐
                 │ Transaction Gateway     │
                 │ (Spring Boot)           │
                 └────────┬────────────────┘
                          │
              ┌───────────┼───────────┐
              │           │           │
              ▼           ▼           ▼
      ┌──────────┐ ┌──────────┐ ┌──────────┐
      │PostgreSQL│ │  Redis   │ │ Swagger  │
      └──────────┘ └──────────┘ └──────────┘
                          │
                          ▼
                 ┌─────────────────┐
                 │ Kafka Topic     │
                 │ payment-events  │
                 └────────┬────────┘
                          │
                          ▼
                 ┌─────────────────┐
                 │ Ledger Service  │
                 │ (Spring Boot)   │
                 └────────┬────────┘
                          │
              ┌───────────┼───────────┐
              │                       │
              ▼                       ▼
       ┌────────────┐         ┌────────────┐
       │ PostgreSQL │         │ Kafka      │
       │ Ledger DB  │         │ Result     │
       └────────────┘         └─────┬──────┘
                                    │
                                    ▼
                     ┌────────────────────────┐
                     │ Transaction Gateway    │
                     │ Result Consumer        │
                     └────────────────────────┘
```

---

## Technologies Used

| Technology | Version |
|------------|----------|
| Java | 21 |
| Spring Boot | 3.5 |
| PostgreSQL | 16 |
| Redis | 7 |
| Apache Kafka | Latest |
| Docker | Latest |
| JUnit 5 | Latest |
| Mockito | Latest |
| JaCoCo | 0.8.12 |
| GitHub Actions | CI/CD |
| Swagger OpenAPI | 2.8.9 |

---

## Implemented Features

### Payment Processing

- Create Payment
- Retrieve Transaction
- Retrieve All Transactions

### Ledger Management

- Retrieve All Ledger Entries
- Retrieve User Ledger History

### Validation

- Insufficient Balance Validation
- Duplicate Payment Prevention
- Transaction Not Found Handling

### Idempotency

Redis based idempotency key support prevents duplicate payment requests.

### Event Driven Processing

Kafka based communication between services.

### Global Exception Handling

Custom exceptions:

- DuplicatePaymentException
- TransactionNotFoundException
- InsufficientBalanceException

### API Documentation

Swagger UI available for both services.

### Docker Support

Containerized deployment for:

- Transaction Gateway
- Ledger Service
- PostgreSQL
- Redis
- Kafka

### CI/CD

GitHub Actions pipeline:

- Build Validation
- Test Execution
- JaCoCo Coverage Validation
- Maven Packaging

---

## Test Coverage

### Ledger Service

```text
92%
```

### Transaction Gateway

```text
84%
```

Coverage enforced through JaCoCo.

Minimum coverage threshold:

```text
80%
```

---

## Running Locally

### Build

```bash
mvn clean install
```

### Run Containers

```bash
docker compose up -d
```

### Stop Containers

```bash
docker compose down
```

---

## Swagger URLs

### Transaction Gateway

```text
http://localhost:8080/swagger-ui/index.html
```

### Ledger Service

```text
http://localhost:8081/swagger-ui/index.html
```

---

## CI/CD

GitHub Actions automatically:

- Executes tests
- Validates coverage
- Builds applications

Pipeline status:

```text
PASSING
```

---

## Future Enhancements

- Dead Letter Topic (DLT)
- Kafka Retry Mechanism
- Prometheus Metrics
- Grafana Dashboard
- Kubernetes Deployment
- Distributed Tracing
- OAuth2 Authentication
- Circuit Breaker using Resilience4j

---

## Author

Bangaru Sri Sai Varun

SwiftPay Hackathon Submission
