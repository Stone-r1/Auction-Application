package org.example.shared.infrastructure;

import org.example.shared.domain.AuctionEventPublisher;
import org.example.shared.events.AuctionClosedEvent;
import org.example.shared.events.BidPlacedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import static org.example.shared.data.RabbitConstants.*;


@Component
public class RabbitMqEventPublisher implements AuctionEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    public RabbitMqEventPublisher(
            RabbitTemplate rabbitTemplate
    ) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /*
    @Override
    public void publish(
            AuctionStartedEvent event
    ) {
        rabbitTemplate.convertAndSend(
                AUCTION_EXCHANGE,
                AUCTION_STARTED_ROUTING_KEY,
                event
        );
    }
    */

    @Override
    public void publish(
            AuctionClosedEvent event
    ) {
        rabbitTemplate.convertAndSend(
                AUCTION_EXCHANGE,
                AUCTION_CLOSED_ROUTING_KEY,
                event
        );
    }

    @Override
    public void publish(
            BidPlacedEvent event
    ) {
        rabbitTemplate.convertAndSend(
                AUCTION_EXCHANGE,
                BID_PLACED_ROUTING_KEY,
                event
        );
    }
}
