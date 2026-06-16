package org.example.auction.domain.services;


import org.example.auction.domain.entities.Auction;
import org.example.auction.domain.entities.Bid;
import org.example.auction.domain.exceptions.AuctionNotFoundException;
import org.example.auction.domain.exceptions.AuctionNotStartedException;
import org.example.auction.domain.exceptions.InsufficientBidAmountException;
import org.example.auction.domain.repositories.AuctionRepository;
import org.example.auction.domain.repositories.BidRepository;
import org.example.shared.data.AuctionState;
import org.example.shared.infrastructure.exceptions.UserNotFoundException;
import org.example.user.domain.entities.User;
import org.example.user.domain.repositories.AuthenticationRepository;


public class BidService {
    private final BidRepository bidRepository;
    private final AuthenticationRepository authenticationRepository;
    private final AuctionRepository auctionRepository;

    public BidService(
            BidRepository bidRepository,
            AuthenticationRepository authenticationRepository,
            AuctionRepository auctionRepository
    ) {
        this.bidRepository = bidRepository;
        this.authenticationRepository = authenticationRepository;
        this.auctionRepository = auctionRepository;
    }

    private User getAuctionParticipant(
            Long userId
    ) {
        return authenticationRepository
                .findByUserId(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User was not found in the database"
                        )
                );
    }

    private Auction getAuctionByAuctionId(
            Long auctionId
    ) {
        return auctionRepository
                .findAuctionByAuctionId(auctionId)
                .orElseThrow(() ->
                        new AuctionNotFoundException(
                                "Auction was not found in the database"
                        )
                );
    }

    public Bid placeBid(
          Bid bidToPlace
    ) {
        User participant = getAuctionParticipant(bidToPlace.getUserId());
        Auction auction = getAuctionByAuctionId(bidToPlace.getAuctionId());

        AuctionState currentState = auction.getAuctionState();
        Double currentMaxBid = auction.getMaxBid();
        Double requestedBidAmount = bidToPlace.getAmount();

        if (currentState != AuctionState.ONGOING) {
            throw new AuctionNotStartedException(
                    "Auction is not ongoing. Current state: " + currentState
            );
        }

        if (currentMaxBid != null && currentMaxBid >= requestedBidAmount) {
            throw new InsufficientBidAmountException(
                    "Current max bid amount: " + currentMaxBid +
                    " is greater or equal to bid requested: " + requestedBidAmount
            );
        }

        auction.setMaxBid(requestedBidAmount);
        auction.setWinnerId(participant.getUserId());

        return bidRepository.save(bidToPlace);
    }
}
