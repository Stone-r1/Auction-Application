package org.example.auction.application;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


public record CreateAuctionRequest(

        @NotBlank(message = "Item name is required")
        String itemName,

        @NotBlank(message = "Item description is required")
        String itemDescription,

        @Min(0)
        Double startingPrice,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        @NotNull(message = "Auction start date is required")
        LocalDateTime startDate,

        @Min(1) @Max(120)
        @NotNull(message = "Duration of the auction is required [in hours]")
        Long duration
) {}
