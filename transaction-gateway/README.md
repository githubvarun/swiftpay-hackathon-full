# SwiftPay Hackathon

## Transaction Gateway

### Features Implemented

- Spring Boot 3
- PostgreSQL Integration
- JPA/Hibernate
- Payment Creation API
- Transaction Persistence

### API

POST /v1/payments

Request:

{
  "senderId": 1,
  "receiverId": 2,
  "amount": 500,
  "currency": "INR"
}

Response:

Payment Accepted
