# Auction App

Idea is to not separate the project into microservices and make on monolith project.
One repository, one Spring Boot app, but with strict internal boundaries that mirror what microservices would look like.


## Architecture
```markdown
org.example/
  auction/        [core]
    domain/
    application/
    infrastructure/
  notification/   [worker]
    domain/
    application/
    infrastructure/
  user/           [standalone]
    domain/
    application/
    infrastructure/
  shared/
    config/       [RabbitMQ config, Security config]
    events/       [BidPlacedEvent, AuctionExpiredEvent] — shared between modules
```

## Flow
```markdown 
User places bid
      |
BidService (synchronous, @Transactional with DB lock)
      | bid accepted - returns immediately to user
      |
Publishes BidPlacedEvent → RabbitMQ
      |
Notification Worker consumes event
      |
Notifies outbid users
```

## Security Format
```markdown
Login Request → AuthenticationService.getUserByUsername → AuthenticationRepository → Database
```