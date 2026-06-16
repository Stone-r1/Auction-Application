package org.example.shared.domain;

import org.example.shared.events.AuctionClosedEvent;
import org.example.shared.events.AuctionStartedEvent;
import org.example.shared.events.BidPlacedEvent;


public interface AuctionEventPublisher {
    void publish(AuctionStartedEvent event);
    void publish(AuctionClosedEvent event);
    void publish(BidPlacedEvent event);
}
