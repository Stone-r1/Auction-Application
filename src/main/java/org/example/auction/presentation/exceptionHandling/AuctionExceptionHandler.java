package org.example.auction.presentation.exceptionHandling;


import org.example.auction.domain.exceptions.*;
import org.example.shared.presentation.exceptionHandlers.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;
import java.util.Map;


@RestControllerAdvice
public class AuctionExceptionHandler {

    @ExceptionHandler(AuctionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleAuctionNotFound(
            AuctionNotFoundException exception
    ) {
        return new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                "Auction not found",
                Map.of("auctionId", String.valueOf(exception.getAuctionId()))
        );
    }

    @ExceptionHandler(AuctionNotStartedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorMessage handleAuctionNotStarted(
            AuctionNotStartedException exception
    ) {
        return new ErrorMessage(
                HttpStatus.CONFLICT.value(),
                new Date(),
                "Auction is not accepting bids",
                Map.of(
                        "auctionId", String.valueOf(exception.getAuctionId()),
                        "currentState", exception.getCurrentState().name()
                )
        );
    }

    @ExceptionHandler(InsufficientBidAmountException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleInsufficientBid(
            InsufficientBidAmountException exception
    ) {
        return new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                "Bid amount is too low",
                Map.of(
                        "requestedAmount", String.valueOf(exception.getRequested()),
                        "minimumRequired", String.valueOf(exception.getCurrent())
                )
        );
    }

    @ExceptionHandler(AuctionAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorMessage handleAuctionAlreadyExists(
            AuctionAlreadyExistsException exception
    ) {
        return new ErrorMessage(
                HttpStatus.CONFLICT.value(),
                new Date(),
                "Auction already exists",
                Map.of("itemName", exception.getItemName())
        );
    }

    @ExceptionHandler(InvalidBidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleInvalidBidException(
            InvalidBidException exception
    ) {
        return new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                "Invalid Bid Exception",
                Map.of("reason", exception.getLocalizedMessage())
        );
    }
}
