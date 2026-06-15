package org.example.auction.domain.services;


import org.example.auction.domain.entities.Auction;
import org.example.auction.domain.repositories.AuctionRepository;

import java.util.List;

public class AuctionService {
    private final AuctionRepository auctionRepository;

    public AuctionService(
            AuctionRepository auctionRepository
    ) {
        this.auctionRepository = auctionRepository;
    }

    public List<Auction> getAllAuctions() {
        return auctionRepository.findAllAuctions();
    }
}
