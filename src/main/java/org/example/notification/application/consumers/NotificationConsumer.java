package org.example.notification.application.consumers;


import org.example.notification.domain.services.NotificationService;
import org.example.shared.events.AuctionClosedEvent;
import org.example.shared.events.AuctionStartedEvent;
import org.example.shared.events.BidPlacedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static org.example.shared.data.RabbitConstants.NOTIFICATION_QUEUE;


@Component
@RabbitListener(queues = NOTIFICATION_QUEUE)
public class NotificationConsumer {
    private final NotificationService notificationService;

    public NotificationConsumer(
            NotificationService notificationService
    ) {
        this.notificationService = notificationService;
    }

    @RabbitHandler
    public void handleAuctionClosed(
            AuctionClosedEvent event
    ) {
        notificationService.notifyAuctionClosed(event);
    }

    @RabbitHandler
    public void handleBidPlaced(
            BidPlacedEvent event
    ) {
        notificationService.notifyBidderLeading(event);
    }

    // No notifications sent for auction start yet - placeholder for future use
    @RabbitHandler
    public void handleAuctionStarted(
            AuctionStartedEvent event
    ) {}
}

