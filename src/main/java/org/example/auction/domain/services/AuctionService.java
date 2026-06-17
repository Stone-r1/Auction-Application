package org.example.auction.domain.services;


import org.example.auction.domain.exceptions.AuctionStateException;
import org.example.auction.domain.exceptions.AuctionNotStartedException;
import org.example.shared.data.AuctionState;
import org.example.auction.domain.entities.Auction;
import org.example.auction.domain.exceptions.AuctionAlreadyExistsException;
import org.example.auction.domain.repositories.AuctionRepository;
import org.example.shared.domain.PageQuery;
import org.example.shared.domain.PageResult;

import java.time.LocalDateTime;
import java.util.List;


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

    public String createAuction(Auction auction) {
        auctionRepository.findAuctionBySellerIdAndItemName(
                auction.getSellerId(), auction.getItemName()
        ).ifPresent(existing -> {
            throw new AuctionAlreadyExistsException(auction.getItemName());
        });

        auction.setAuctionState(AuctionState.PENDING);
        auctionRepository.save(auction);
        return "Auction was registered successfully";
    }

    public List<Auction> getAuctionsByAuctionStateAndStartDateBefore(
            AuctionState auctionState,
            LocalDateTime localDateTime
    ) {
        return auctionRepository.findAuctionsByAuctionStateAndStartDateBefore(auctionState, localDateTime);
    }

    public List<Auction> getAuctionsByAuctionStateAndEndDateBefore(
            AuctionState auctionState,
            LocalDateTime localDateTime
    ) {
        return auctionRepository.findAuctionsByAuctionStateAndEndDateBefore(auctionState, localDateTime);
    }

    public void startAuction(
            Auction auction
    ) {
        if (auction.getAuctionState() != AuctionState.PENDING) {
            throw new AuctionNotStartedException(auction.getAuctionId(), auction.getAuctionState());
        }

        auction.setAuctionState(AuctionState.ONGOING);
    }

    public void closeAuction(
            Auction auction
    ) {
        if (auction.getAuctionState() != AuctionState.ONGOING) {
            throw new AuctionStateException("Cannot close a non-ongoing auction.");
        }

        auction.setAuctionState(AuctionState.FINISHED);
    }

}
