package org.example.auction.application.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PlaceBidRequest(
        @Min(0) // it has to be more than previous bid, but user shouldn't be able to place bid less than 0 in general
        @NotNull(message = "Bid amount is required field")
        Double amount
) {}
