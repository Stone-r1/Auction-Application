package org.example.shared.infrastructure.configs;


import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMqConfig {
    public static final String AUCTION_EXCHANGE = "auction.exchange";
    public static final String NOTIFICATION_QUEUE = "notification.queue";

    public static final String AUCTION_CLOSED_ROUTING_KEY = "auction.closed";
    public static final String AUCTION_STARTED_ROUTING_KEY = "auction.started";
    public static final String BID_PLACED_ROUTING_KEY = "bid.placed";

    public static final String DEAD_LETTER_EXCHANGE = "auction.dlx";
    public static final String DEAD_LETTER_QUEUE = "notification.dlq";

    public static final String AUCTION_ROUTING_PATTERN = "auction.*";
    public static final String DLQ_ROUTING_PATTERN = "#";

    // Routes messages to queues based on routing keys and bindings
    @Bean
    public TopicExchange auctionExchange() {
        return new TopicExchange(AUCTION_EXCHANGE);
    }

    @Bean
    public TopicExchange deadLetterExchange() {
        return new TopicExchange(DEAD_LETTER_EXCHANGE);
    }

    // Stores messages until consumers process them
    @Bean
    public Queue notificationQueue() {
        return QueueBuilder
                .durable(NOTIFICATION_QUEUE) // Persists even when RabbitMQ is restarted
                .deadLetterExchange(DEAD_LETTER_EXCHANGE) // Failed or expired messages are forwarded to the DLX
                .build();
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder
                .durable(DEAD_LETTER_QUEUE)
                .build();
    }

    // Routes messages matching "auction.*" from the exchange to the queue
    @Bean
    public Binding notificationBinding() {
        return BindingBuilder
                .bind(notificationQueue())
                .to(auctionExchange())
                .with(AUCTION_ROUTING_PATTERN);
    }

    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder
                .bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with(DLQ_ROUTING_PATTERN);
    }

    // Note on calling methods - spring returns the singleton bean instance, not a new object.
}
