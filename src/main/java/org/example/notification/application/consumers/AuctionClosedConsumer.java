package org.example.notification.application.consumers;


import org.example.notification.domain.services.NotificationService;
import org.example.shared.events.AuctionClosedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static org.example.shared.data.RabbitConstants.NOTIFICATION_QUEUE;


@Component
public class AuctionClosedConsumer {
    private final NotificationService notificationService;

    public AuctionClosedConsumer(
            NotificationService notificationService
    ) {
        this.notificationService = notificationService;
    }

    // handles removal automatically
    @RabbitListener(queues = NOTIFICATION_QUEUE)
    public void handleAuctionClosed(
            AuctionClosedEvent event
    ) {
        notificationService.notifyAuctionClosed(event);
    }
}
