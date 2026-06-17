# Auction App

A **modular monolith** Spring Boot application for online auctions. The project intentionally avoids splitting into microservices.
instead it enforces strict internal module boundaries that mirror what a microservices architecture would look like, all within a single deployable artifact.
Featuring **Domain-Driver Development** in unison with **Hexagonal Architecture.** The project was created for demonstrating and learning purposes.
---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4.0.6 |
| Database | PostgreSQL |
| Messaging | RabbitMQ (Spring AMQP) |
| Auth | JWT (jjwt 0.13.0) + BCrypt |
| Email | Spring Mail (Gmail SMTP) |
| Build | Maven |
| Utilities | Lombok, spring-dotenv |

---

## Architecture

The codebase is divided into three feature modules and one shared module, each following a layered structure (`domain → application → infrastructure → presentation`):

```
org.example/
  auction/          ← Core auction logic
    domain/         ← Entities (Auction, Bid), repositories, services
    application/    ← Use cases (AuctionUseCase, BidUseCase, AuctionLifeCycleUseCase)
    infrastructure/ ← JPA adapters, scheduler, configs
    presentation/   ← REST controllers, exception handlers

  notification/     ← Async notification worker
    domain/         ← NotificationService, repositories
    application/    ← RabbitMQ consumer (NotificationConsumer)
    infrastructure/ ← Email/JPA adapters

  user/             ← Authentication & identity
    domain/         ← Entities (User, VerificationToken), services
    application/    ← Use cases (LoginUseCase, RegisterUseCase, VerificationUseCase)
    infrastructure/ ← JPA/mail adapters, JWT, configs
    presentation/   ← AuthenticationController, exception handler

  shared/           ← Cross-module contracts
    domain/         ← Ports: AuctionEventPublisher, TokenRepository, EmailSenderRepository
    events/         ← BidPlacedEvent, AuctionStartedEvent, AuctionClosedEvent
    data/           ← AuctionState enum, RabbitConstants, PageQuery/PageResult
    infrastructure/ ← RabbitMqConfig, SecurityConfig, JwtAuthFilter, RabbitMqEventPublisher
    presentation/   ← Shared error response model
```

---

## Flows

### Bid Placement
```
Authenticated user  →  POST /auction/{id}/placeBid
                              │
                   BidUseCase (validates bid, @Transactional)
                              │
                   BidService (DB lock — ensures only one winner)
                              │ bid accepted
                              │
                   Publishes BidPlacedEvent → RabbitMQ
                              │
                   NotificationConsumer (async)
                              │
                   Emails the previously-leading bidder that they were outbid
```

### Auction Lifecycle (Scheduler, every 60 s)
```
AuctionScheduler
  ├── startPendingAuctions()  →  PENDING  → ONGOING  +  AuctionStartedEvent
  └── closeExpiredAuctions()  →  ONGOING  → CLOSED   +  AuctionClosedEvent  →  notifies winner & seller by email
```

### Authentication
```
POST /register  →  RegisterUseCase  →  BCrypt hash  →  save User (disabled)
                                   └→  send verification email with token

GET  /verify    →  VerificationUseCase  →  enable User

POST /login     →  LoginUseCase  →  validate credentials + enabled check  →  return JWT
```

---

## API Endpoints

All endpoints except `/register`, `/login`, and `/verify` require a valid JWT in the `Authorization: Bearer <token>` header.

### Authentication

| Method | Path | Description |
|---|---|---|
| `POST` | `/register` | Register a new user. Sends a verification email. |
| `POST` | `/login` | Authenticate and receive a JWT. |
| `GET` | `/verify?token=<token>` | Verify email address using the token from the email. |

#### Register — request body
```json
{
  "username": "alice",
  "password": "P@ssw0rd1",
  "email": "alice@example.com",
  "birthDate": "1990-01-01"
}
```

#### Login — request body
```json
{
  "username": "alice",
  "password": "P@ssw0rd1"
}
```

---

### Auctions

| Method | Path | Description |
|---|---|---|
| `GET` | `/auction/all?page=0&size=20` | List auctions (paginated). |
| `POST` | `/auction/create` | Create a new auction. |
| `POST` | `/auction/{auctionId}/placeBid` | Place a bid on an active auction. |

#### Create Auction — request body
```json
{
  "itemName": "Vintage Guitar",
  "itemDescription": "1967 Fender Stratocaster",
  "startingPrice": 500.00,
  "startDate": "2026-07-01T10:00:00",
  "duration": 24
}
```
> `duration` is in hours. `endDate` is calculated as `startDate + duration`.

#### Place Bid — request body
```json
{
  "amount": 750.00
}
```

---

## Auction States

```
PENDING  ──(scheduler, start date reached)──►  ONGOING  ──(scheduler, end date reached)──►  CLOSED
```

The scheduler polls every **60 seconds**.

---

## RabbitMQ Topology

| Resource | Name |
|---|---|
| Exchange | `auction.exchange` (topic) |
| Notification queue | `notification.queue` |
| Dead-letter exchange | `auction.dlx` |
| Dead-letter queue | `notification.dlq` |
| Routing keys | `auction.started`, `auction.closed`, `auction.bid` |

---

## Configuration

### Environment Variables

Copy `src/main/resources/application-dev.properties.example` to `application-dev.properties` and fill in the values:

| Variable | Description |
|---|---|
| `DB_URL` | JDBC URL, e.g. `jdbc:postgresql://localhost:5432/auction` |
| `DB_USERNAME` | PostgreSQL username |
| `DB_PASSWORD` | PostgreSQL password |
| `JWT_SECRET_KEY` | Secret for signing JWTs (defaults to a placeholder if omitted) |
| `JWT_EXPIRATION` | Token lifetime in seconds (default `36000`) |
| `MAIL_USERNAME` | Gmail address used to send emails |
| `MAIL_PASSWORD` | Gmail App Password |
| `APP_BASE_URL` | Public base URL for verification links, e.g. `http://localhost:8080` |
| `RABBIT_MQ_USERNAME` | RabbitMQ username (default `guest`) |
| `RABBIT_MQ_PASSWORD` | RabbitMQ password (default `guest`) |

> The app uses [spring-dotenv](https://github.com/paulschwarz/spring-dotenv) so you can also supply these via a `.env` file in the project root.

---

## Running Locally

### Prerequisites
- Java 21+
- Maven 3.9+
- PostgreSQL instance
- RabbitMQ instance

### Steps

```bash
# 1. Clone the repo
git clone <repo-url>
cd Auction-App

# 2. Set up environment
cp src/main/resources/application-dev.properties.example src/main/resources/application-dev.properties
# edit application-dev.properties with your values

# 3. Build & run
./mvnw spring-boot:run
```

The application starts on **port 8080** by default.

---

## Running Tests

```bash
./mvnw test
```

Tests cover use cases, domain services, and REST controllers using JUnit 5, Mockito, and Spring's test slice annotations.
