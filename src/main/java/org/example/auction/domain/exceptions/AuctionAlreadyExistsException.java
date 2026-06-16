package org.example.auction.domain.exceptions;

import org.example.shared.domain.exceptions.AppException;


public class AuctionAlreadyExistsException extends AppException {

    private final String itemName;

    public AuctionAlreadyExistsException(
            String itemName
    ) {
        super("You already have an active auction for item '" + itemName + "'.");
        this.itemName = itemName;
    }

    public AuctionAlreadyExistsException(
            String itemName,
            Throwable cause
    ) {
        super("You already have an active auction for item '" + itemName + "'.", cause);
        this.itemName = itemName;
    }

    public String getItemName() {
        return itemName;
    }
}
