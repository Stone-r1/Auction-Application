package org.example.auction.domain.exceptions;

public class AuctionNotFoundException extends RuntimeException {
    public AuctionNotFoundException(
            String message
    ) {
        super(message);
    }
}
