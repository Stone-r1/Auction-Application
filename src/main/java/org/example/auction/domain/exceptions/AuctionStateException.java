package org.example.auction.domain.exceptions;

import org.example.shared.domain.exceptions.AppException;


public class AuctionStateException extends AppException {
    public AuctionStateException(
            String message
    ) {
        super(message);
    }
}
