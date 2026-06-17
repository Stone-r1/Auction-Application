package org.example.auction.domain.exceptions;


public class InvalidBidException extends RuntimeException {

    public InvalidBidException(
            String message
    ) {
        super(message);
    }

    public InvalidBidException(
            String message,
            Throwable cause
    ) {
        super(message, cause);
    }
}
