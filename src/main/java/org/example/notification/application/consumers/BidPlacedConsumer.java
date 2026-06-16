package org.example.notification.application.consumers;


import org.example.notification.domain.services.NotificationService;
import org.example.shared.events.BidPlacedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static org.example.shared.data.RabbitConstants.NOTIFICATION_QUEUE;


@Component
public class BidPlacedConsumer {
    private final NotificationService notificationService;

    public BidPlacedConsumer(
            NotificationService notificationService
    ) {
        this.notificationService = notificationService;
    }

    @RabbitListener(queues = NOTIFICATION_QUEUE)
    public void handleNotifyBidder(
            BidPlacedEvent bidPlacedEvent
    ) {
        notificationService.notifyBidderLeading(bidPlacedEvent);
    }
}
