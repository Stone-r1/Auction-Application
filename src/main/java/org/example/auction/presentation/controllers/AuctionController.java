package org.example.auction.presentation.controllers;


import org.example.auction.application.AuctionUseCase;
import org.example.auction.domain.entities.Auction;
import org.example.shared.domain.PageResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


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
}
