package org.example.auction.domain.services;


import org.example.auction.domain.entities.Auction;
import org.example.auction.domain.repositories.AuctionRepository;
import org.example.shared.domain.PageQuery;
import org.example.shared.domain.PageResult;


public class AuctionService {
    private final AuctionRepository auctionRepository;

    public AuctionService(
            AuctionRepository auctionRepository
    ) {
        this.auctionRepository = auctionRepository;
    }

    public PageResult<Auction> getAllAuctions(
            PageQuery pageQuery
    ) {
        return auctionRepository.findAll(pageQuery);
    }
}
