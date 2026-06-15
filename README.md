# SwiftPay - Event Driven Payment Processing Platform

## Overview

SwiftPay is an event-driven payment processing platform built using Spring Boot, Apache Kafka, PostgreSQL, Redis, Docker, and GitHub Actions.

The system demonstrates how modern payment systems process transactions asynchronously using microservices and event-driven communication patterns.

Key capabilities include:

* Event-driven payment processing
* Kafka-based asynchronous communication
* Redis-based idempotency handling
* Retry mechanism with Dead Letter Topic (DLT)
* Separate databases per microservice
* Swagger/OpenAPI documentation
* Dockerized deployment
* CI/CD using GitHub Actions
* Health monitoring using Spring Actuator
* High unit test coverage using JUnit and Mockito

---
## Architecture Flow

1. Client sends payment request to Transaction Gateway.
2. Transaction Gateway stores transaction as PENDING.
3. Transaction Gateway publishes Payment Event to Kafka.
4. Ledger Service consumes the event.
5. Ledger Service validates balance and updates ledger.
6. Ledger Service publishes Payment Result Event.
7. Transaction Gateway consumes result event.
8. Transaction status is updated to COMPLETED or FAILED.

---

# Architecture Highlights

* Event Driven Architecture
* Apache Kafka Messaging
* Separate Databases per Service
* Retry Topics and Dead Letter Topic
* Redis Idempotency Protection
* Swagger Documentation
* Dockerized Deployment
* CI/CD Automation
* Health Monitoring
* High Test Coverage

---

# Technology Stack

| Technology      | Purpose                   |
| --------------- | ------------------------- |
| Java 21         | Programming Language      |
| Spring Boot     | Microservices Framework   |
| Spring Data JPA | Database Access           |
| PostgreSQL      | Persistent Storage        |
| Apache Kafka    | Event Streaming           |
| Redis           | Idempotency Handling      |
| Docker          | Containerization          |
| Docker Compose  | Environment Orchestration |
| Swagger/OpenAPI | API Documentation         |
| JUnit 5         | Unit Testing              |
| Mockito         | Mocking                   |
| GitHub Actions  | CI/CD                     |

---

# Microservices

## Transaction Gateway Service

### Responsibilities

* Accept payment requests
* Prevent duplicate payments
* Store payment transactions
* Publish payment events to Kafka
* Consume payment result events
* Update transaction status

### Port

8080

---

## Ledger Service

### Responsibilities

* Consume payment events
* Validate account balances
* Process fund transfers
* Maintain ledger entries
* Publish payment result events
* Retry failed processing

### Port

8081

---

# Event Flow

```text
Client
  |
  v
Transaction Gateway
  |
  | Payment Event
  v
Kafka
  |
  v
Ledger Service
  |
  | Payment Result Event
  v
Kafka
  |
  v
Transaction Gateway
```

---

# Kafka Topics

| Topic                  | Purpose           |
| ---------------------- | ----------------- |
| payment-events         | Payment Requests  |
| payment-result-events  | Payment Results   |
| payment-events-retry-* | Retry Processing  |
| payment-events-dlt     | Dead Letter Topic |

---

# Resilience - Retry Mechanism

Requirement:

Retry Kafka consumers when database becomes temporarily unavailable.

Implementation:

* Spring Kafka Retryable Topics
* Exponential Backoff Strategy
* Dead Letter Topic (DLT)

Configuration:

```java
@RetryableTopic(
        attempts = "4",
        backoff = @Backoff(
                delay = 5000,
                multiplier = 2.0
        )
)
```

Retry Flow:

```text
Attempt 1
   |
5 seconds
   |
Attempt 2
   |
10 seconds
   |
Attempt 3
   |
20 seconds
   |
Attempt 4
   |
Dead Letter Topic
```

If the database becomes available during retry execution, the transaction is processed successfully.

---

# API Documentation

## Transaction Gateway Swagger

http://localhost:8080/swagger-ui/index.html

---

## Ledger Service Swagger

http://localhost:8081/swagger-ui/index.html

---

# Health Check Endpoints

## Transaction Gateway

http://localhost:8080/actuator/health

---

## Ledger Service

http://localhost:8081/actuator/health

---

# Docker Deployment

## Build Images

```bash
docker build -t transaction-gateway:1.0 ./transaction-gateway

docker build -t ledger-service:1.0 ./ledger-service
```

## Start Complete Platform

```bash
docker compose up -d
```

## Stop Platform

```bash
docker compose down
```

---

# Running Locally

## Clone Repository

```bash
git clone <repository-url>

cd swiftpay-hackathon-full
```

## Build Project

```bash
mvn clean install
```

## Start Infrastructure

```bash
docker compose up -d
```

## Verify Containers

```bash
docker ps
```

---

# Database Design

The solution follows the Database Per Service pattern.

| Service             | Database       |
| ------------------- | -------------- |
| Transaction Gateway | transaction_db |
| Ledger Service      | ledger_db      |

Benefits:

* Loose coupling
* Independent scaling
* Service autonomy
* Better fault isolation

---

# Redis Idempotency

Redis is used to prevent duplicate payment submissions.

Generated Key:

```text
senderId-receiverId-amount
```

Example:

```text
1-2-100
```

Duplicate requests are rejected before processing.

---

# Observability

Implemented Features:

* Structured Logging
* Health Check Endpoints
* Kafka Retry Logging
* Dead Letter Topic Logging
* Spring Boot Actuator

---

# CI/CD Pipeline

GitHub Actions workflow performs:

* Compile Java code
* Execute unit tests
* Execute integration tests
* Build Docker images

Workflow Location:

```text
.github/workflows/
```

---

# Test Coverage

## Coverage Summary

| Service             | Coverage |
| ------------------- | -------- |
| Transaction Gateway | 96%      |
| Ledger Service      | 94%      |

---

## Transaction Gateway Coverage


Coverage Achieved: 96%

---

## Ledger Service Coverage


Coverage Achieved: 94%

---

# Sample Payment Request

```json
{
   "senderId": 1,
   "receiverId": 2,
   "amount": 100,
   "currency": "INR"
}
```

Sample Response:

```json
{
   "transactionId": "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx",
   "status": "PENDING",
   "message": "Payment Accepted"
}
```

## Load Testing Evidence

Tool: Apache JMeter

Configuration:

* Threads: 100
* Ramp-Up: 5 seconds
* Loop Count: 10000

Results:

* Total Transactions: 1,000,000
* Throughput: 2271 TPS
* Error Rate: 0.02%

Evidence Included:

* JMeter Test Plan (`jmeter-1m-load-test.jmx`)
* JMeter Execution Screenshots
* Wireshark PCAP Capture Screenshot

PCAP Trace:

* File Name: `swiftpay_1M_transactions.pcapng`
* Size: ~901 MB
* Due to GitHub file size limitations, the complete PCAP trace is available at:

[Google Drive Link Here] - https://drive.google.com/file/d/1Vdk4zr5LQlEe6il9E4_iLQ8t_aBqh-Fn/view?usp=drivesdk

---

# Future Enhancements

* Analytics Worker (Bonus Service) for real-time payment volume monitoring using Kafka event streams and OLAP analytics.
* Kubernetes Deployment for container orchestration and scaling.
* Prometheus Monitoring for metrics collection.
* Grafana Dashboards for real-time observability.
* API Gateway for centralized routing and request management.
* Authentication & Authorization using Spring Security and JWT.
* Circuit Breaker Pattern for improved fault tolerance and resilience.

---
# Author
Bangaru Sri Sai Varun

SwiftPay – Event Driven Payment Processing Platform

Hackathon Submission
