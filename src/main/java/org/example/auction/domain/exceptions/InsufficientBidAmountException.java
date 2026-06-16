package org.example.auction.domain.exceptions;

public class InsufficientBidAmountException extends RuntimeException {
    public InsufficientBidAmountException(
            String message
    ) {
        super(message);
    }
}
