package org.example.auction.domain.exceptions;

public class AuctionNotStartedException extends RuntimeException {
    public AuctionNotStartedException(
            String message
    ) {
        super(message);
    }
}
