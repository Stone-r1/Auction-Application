package org.example.auction.presentation.controllers;


import jakarta.validation.Valid;
import org.example.auction.application.AuctionUseCase;
import org.example.auction.application.CreateAuctionRequest;
import org.example.auction.domain.entities.Auction;
import org.example.shared.domain.PageResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auction")
public class AuctionController {
    private final AuctionUseCase auctionUseCase;

    public AuctionController(
            AuctionUseCase auctionUseCase
    ) {
        this.auctionUseCase = auctionUseCase;
    }

    @GetMapping("/all")
    public PageResult<Auction> getAllAuctions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return auctionUseCase.getAvailableAuctions(page, size);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createAuction(
            @Valid @RequestBody CreateAuctionRequest createAuctionRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(auctionUseCase.createAuction(createAuctionRequest));
    }
}
