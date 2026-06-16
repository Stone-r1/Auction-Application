package org.example.auction.domain.exceptions;

import lombok.Getter;
import org.example.shared.domain.exceptions.AppException;
import org.example.shared.data.AuctionState;


@Getter
public class AuctionNotStartedException extends AppException {

    private final Long auctionId;
    private final AuctionState currentState;

    public AuctionNotStartedException(
            Long auctionId,
            AuctionState currentState
    ) {
        super("Auction " + auctionId + " is not accepting bids. Current state: " + currentState + ".");
        this.auctionId = auctionId;
        this.currentState = currentState;
    }

    public AuctionNotStartedException(
            Long auctionId,
            AuctionState currentState,
            Throwable cause
    ) {
        super("Auction " + auctionId + " is not accepting bids. Current state: " + currentState + ".", cause);
        this.auctionId = auctionId;
        this.currentState = currentState;
    }
}
