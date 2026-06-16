package org.example.shared.events;

public record BidPlacedEvent(
        Long auctionId,
        Long bidderId,
        Double amount,
        Long previousWinnerId  // the user just outbid - null if first bid
) {}
