package org.example.shared.events;


public record AuctionClosedEvent(
        Long auctionId,
        Long winnerId,    // null if no bids were placed
        Long sellerId,
        Double maxBidAmount // null if no bids
) {}
