package org.example.shared.events;


public record BidPlacedEvent(
        Long auctionId,
        Long bidderId,
        Double amount,
        Long previousWinnerId
) {}
