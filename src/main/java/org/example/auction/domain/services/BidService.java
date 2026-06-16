package org.example.auction.domain.services;


import org.example.auction.domain.entities.Auction;
import org.example.auction.domain.entities.Bid;
import org.example.auction.domain.exceptions.AuctionNotFoundException;
import org.example.auction.domain.exceptions.AuctionNotStartedException;
import org.example.auction.domain.exceptions.InsufficientBidAmountException;
import org.example.auction.domain.repositories.AuctionRepository;
import org.example.auction.domain.repositories.BidRepository;
import org.example.shared.data.AuctionState;


public class BidService {
    private final BidRepository bidRepository;
    private final AuctionRepository auctionRepository;

    public BidService(
            BidRepository bidRepository,
            AuctionRepository auctionRepository
    ) {
        this.bidRepository = bidRepository;
        this.auctionRepository = auctionRepository;
    }

    private Auction getAuctionByAuctionId(
            Long auctionId
    ) {
        return auctionRepository
                .findAuctionByAuctionIdWithLock(auctionId)
                .orElseThrow(() -> new AuctionNotFoundException(auctionId));
    }

    public Bid placeBid(
          Bid bidToPlace
    ) {
        Auction auction = getAuctionByAuctionId(bidToPlace.getAuctionId());

        if (auction.getAuctionState() != AuctionState.ONGOING) {
            throw new AuctionNotStartedException(bidToPlace.getAuctionId(), auction.getAuctionState());
        }

        Double currentMaxBid = auction.getMaxBid();
        Double requestedBidAmount = bidToPlace.getAmount();

        if (currentMaxBid != null && currentMaxBid >= requestedBidAmount) {
            throw new InsufficientBidAmountException(requestedBidAmount, currentMaxBid);
        }

        // update auction entity
        auction.setMaxBid(requestedBidAmount);
        auction.setWinnerId(bidToPlace.getUserId());
        auctionRepository.save(auction);

        return bidRepository.save(bidToPlace);
    }
}
