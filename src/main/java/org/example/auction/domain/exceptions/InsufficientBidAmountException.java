package org.example.auction.domain.exceptions;

import org.example.shared.domain.exceptions.AppException;


public class InsufficientBidAmountException extends AppException {

    private final Double requested;
    private final Double current;

    public InsufficientBidAmountException(
            Double requested,
            Double current
    ) {
        super("Bid amount " + requested + " must exceed the current highest bid of " + current + ".");
        this.requested = requested;
        this.current = current;
    }

    public InsufficientBidAmountException(
            Double requested,
            Double current,
            Throwable cause
    ) {
        super("Bid amount " + requested + " must exceed the current highest bid of " + current + ".", cause);
        this.requested = requested;
        this.current = current;
    }

    public Double getRequested() {
        return requested;
    }

    public Double getCurrent() {
        return current;
    }
}
