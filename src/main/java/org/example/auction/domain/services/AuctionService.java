package org.example.auction.domain.services;


import org.example.auction.domain.data.AuctionState;
import org.example.auction.domain.entities.Auction;
import org.example.auction.domain.repositories.AuctionRepository;
import org.example.shared.domain.PageQuery;
import org.example.shared.domain.PageResult;
import org.example.user.domain.entities.User;
import org.springframework.security.authentication.DisabledException;


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

    public String createAuction(
            Auction auction
    ) {
        User seller = auction.getSeller();

        if (!seller.getEnabled()) {
            throw new DisabledException(
                    "Account with username " + seller.getUsername()
                    + " and email " + seller.getEmail() + " is disabled."
                    + " Validate email before registering auction."
            );
        }

        auction.setAuctionState(AuctionState.PENDING);
        auctionRepository.save(auction);

        return "Auction was registered successfully";
    }
}
