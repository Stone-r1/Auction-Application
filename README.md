# Auction App

![CI](https://github.com/Stone-r1/Auction-Application/actions/workflows/ci.yml/badge.svg)

A **modular monolith** Spring Boot application for online auctions. The project intentionally avoids splitting into microservices —
instead it enforces strict internal module boundaries that mirror what a microservices architecture would look like, all within a single deployable artifact.
Featuring **Domain-Driven Design** in unison with **Hexagonal Architecture.** Created for demonstrating and learning purposes.

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

## API Documentation

Full API reference is available as an OpenAPI 3.0 spec in [`swagger.yaml`](./swagger.yaml).

To preview it in the browser:
```bash
npx @redocly/cli build-docs swagger.yaml --output docs/index.html && open docs/index.html
```

Or paste the file contents into **[editor.swagger.io](https://editor.swagger.io)**.

> All endpoints except `/register`, `/login`, and `/verify` require a valid JWT:
> `Authorization: Bearer <token>`

---

## Auction States

```
PENDING  ──(scheduler, start date reached)──►  ONGOING  ──(scheduler, end date reached)──►  FINISHED
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

## Running with Docker (recommended)

### Prerequisites
- [Docker Desktop](https://www.docker.com/products/docker-desktop/)

### Steps

```bash
# 1. Clone the repo
git clone https://github.com/Stone-r1/Auction-Application.git
cd Auction-Application

# 2. Set up secrets
cp .env.example .env
# edit .env with your values (DB password, mail credentials, etc.)

# 3. Build and start everything (app + PostgreSQL + RabbitMQ)
docker compose up --build
```

The app starts on **http://localhost:8080**.
RabbitMQ management UI is available at **http://localhost:15672** (default: guest/guest).

| Command | What it does |
|---|---|
| `docker compose up --build -d` | Start in background |
| `docker compose logs -f app` | Tail app logs |
| `docker compose down` | Stop everything (data kept) |
| `docker compose down -v` | Stop and wipe all data volumes |

---

## Running Locally (without Docker)

### Prerequisites
- Java 21+
- Maven 3.9+
- PostgreSQL instance
- RabbitMQ instance

### Steps

```bash
# 1. Clone the repo
git clone https://github.com/Stone-r1/Auction-Application.git
cd Auction-Application

# 2. Set up environment
cp src/main/resources/application-dev.properties.example src/main/resources/application-dev.properties
# edit application-dev.properties with your values

# 3. Build & run
./mvnw spring-boot:run
```

### Environment Variables

| Variable | Description |
|---|---|
| `DB_URL` | JDBC URL, e.g. `jdbc:postgresql://localhost:5432/auctiondb` |
| `DB_USERNAME` | PostgreSQL username |
| `DB_PASSWORD` | PostgreSQL password |
| `JWT_SECRET_KEY` | Secret for signing JWTs |
| `JWT_EXPIRATION` | Token lifetime in seconds (default `36000`) |
| `MAIL_USERNAME` | Gmail address used to send emails |
| `MAIL_PASSWORD` | Gmail App Password |
| `APP_BASE_URL` | Public base URL for verification links |
| `RABBIT_MQ_USERNAME` | RabbitMQ username (default `guest`) |
| `RABBIT_MQ_PASSWORD` | RabbitMQ password (default `guest`) |

---

## Running Tests

```bash
./mvnw test
```

Tests cover use cases, domain services, and REST controllers using JUnit 5, Mockito, and Spring's test slice annotations.

The CI pipeline runs tests automatically on every push and pull request — including real PostgreSQL and RabbitMQ service containers.
