package org.example.shared.infrastructure;

import org.example.shared.domain.AuctionEventPublisher;
import org.example.shared.events.AuctionClosedEvent;
import org.example.shared.events.AuctionStartedEvent;
import org.example.shared.events.BidPlacedEvent;
import org.example.shared.infrastructure.configs.RabbitMqConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;


@Component
public class RabbitMqEventPublisher implements AuctionEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    public RabbitMqEventPublisher(
            RabbitTemplate rabbitTemplate
    ) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publish(
            AuctionStartedEvent event
    ) {
        rabbitTemplate.convertAndSend(
                RabbitMqConfig.AUCTION_EXCHANGE,
                RabbitMqConfig.AUCTION_STARTED_ROUTING_KEY,
                event
        );
    }

    @Override
    public void publish(
            AuctionClosedEvent event
    ) {
        rabbitTemplate.convertAndSend(
                RabbitMqConfig.AUCTION_EXCHANGE,
                RabbitMqConfig.AUCTION_CLOSED_ROUTING_KEY,
                event
        );
    }

    @Override
    public void publish(
            BidPlacedEvent event
    ) {
        rabbitTemplate.convertAndSend(
                RabbitMqConfig.AUCTION_EXCHANGE,
                RabbitMqConfig.BID_PLACED_ROUTING_KEY,
                event
        );
    }
}
