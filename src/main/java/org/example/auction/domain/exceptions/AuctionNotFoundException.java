package org.example.auction.domain.exceptions;

import org.example.shared.domain.exceptions.AppException;


public class AuctionNotFoundException extends AppException {

    private final Long auctionId;

    public AuctionNotFoundException(
            Long auctionId
    ) {
        super("Auction with id " + auctionId + " was not found.");
        this.auctionId = auctionId;
    }

    public AuctionNotFoundException(
            Long auctionId,
            Throwable cause
    ) {
        super("Auction with id " + auctionId + " was not found.", cause);
        this.auctionId = auctionId;
    }

    public Long getAuctionId() {
        return auctionId;
    }
}
