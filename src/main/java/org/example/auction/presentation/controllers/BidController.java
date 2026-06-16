package org.example.auction.presentation.controllers;

import jakarta.validation.Valid;
import org.example.auction.application.dto.PlaceBidRequest;
import org.example.auction.application.orchestrators.BidUseCase;
import org.example.auction.domain.entities.Bid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auction/{auctionId}")
public class BidController {

    private final BidUseCase bidUseCase;

    public BidController(BidUseCase bidUseCase) {
        this.bidUseCase = bidUseCase;
    }

    @PostMapping("/placeBid")
    public ResponseEntity<Bid> placeBid(
            @PathVariable Long auctionId,
            @Valid @RequestBody PlaceBidRequest placeBidRequest
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bidUseCase.placeBid(auctionId, placeBidRequest));
    }
}
