package org.example.auction.presentation.controllers;


import org.example.auction.application.AuctionUseCase;
import org.example.auction.domain.entities.Auction;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/auction")
public class AuctionController {
    private final AuctionUseCase auctionUseCase;

    public AuctionController(
            AuctionUseCase auctionUseCase
    ) {
        this.auctionUseCase = auctionUseCase;
    }

    @PostMapping("/all")
    public List<Auction> getAllAuctions() {
        return auctionUseCase.getAvailableAuctions();
    }
}
