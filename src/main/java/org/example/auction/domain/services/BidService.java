package org.example.auction.domain.services;


import org.example.auction.domain.entities.Auction;
import org.example.auction.domain.entities.Bid;
import org.example.auction.domain.exceptions.AuctionNotFoundException;
import org.example.auction.domain.exceptions.AuctionNotStartedException;
import org.example.auction.domain.exceptions.InsufficientBidAmountException;
import org.example.auction.domain.exceptions.InvalidBidException;
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
                .orElseThrow(() ->
                        new AuctionNotFoundException(
                                auctionId
                        )
                );
    }

    private void updateAuctionEntity(
            Auction auction,
            Bid bid
    ) {
        auction.setMaxBid(bid.getAmount());
        auction.setWinnerId(bid.getUserId());
        auctionRepository.save(auction);
    }

    public Long placeBid(
          Bid bidToPlace
    ) {
        Auction auction = getAuctionByAuctionId(bidToPlace.getAuctionId());

        if (auction.getAuctionState() != AuctionState.ONGOING) {
            throw new AuctionNotStartedException(bidToPlace.getAuctionId(), auction.getAuctionState());
        }

        if (bidToPlace.getUserId().equals(auction.getSellerId())) {
            throw new InvalidBidException(
                    "Auction owner is not able to place bids"
            );
        }

        Double currentMaxBid = auction.getMaxBid();
        Double requestedBidAmount = bidToPlace.getAmount();

        Double threshold = currentMaxBid != null ? currentMaxBid : auction.getStartingPrice();
        if (requestedBidAmount <= threshold) {
            throw new InsufficientBidAmountException(requestedBidAmount, threshold);
        }

        // at this point we are sure about outbid
        Long previousWinner = auction.getWinnerId();
        updateAuctionEntity(auction, bidToPlace);
        bidRepository.save(bidToPlace);
        return previousWinner;
    }
}
