package org.example.auction.application;


import org.example.auction.domain.entities.Auction;
import org.example.auction.domain.services.AuctionService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AuctionUseCase {
    private final AuctionService auctionService;

    public AuctionUseCase(
            AuctionService auctionService
    ) {
        this.auctionService = auctionService;
    }

    public List<Auction> getAvailableAuctions() {
        return auctionService.getAllAuctions();
    }
}
