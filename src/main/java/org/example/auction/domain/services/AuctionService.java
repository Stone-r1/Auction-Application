package org.example.auction.domain.services;


import org.example.shared.data.AuctionState;
import org.example.auction.domain.entities.Auction;
import org.example.auction.domain.repositories.AuctionRepository;
import org.example.shared.domain.PageQuery;
import org.example.shared.domain.PageResult;
import org.example.shared.infrastructure.exceptions.UserNotFoundException;
import org.example.user.domain.entities.User;
import org.example.user.domain.repositories.AuthenticationRepository;
import org.springframework.security.authentication.DisabledException;


public class AuctionService {
    private final AuctionRepository auctionRepository;
    private final AuthenticationRepository authenticationRepository;

    public AuctionService(
            AuctionRepository auctionRepository,
            AuthenticationRepository authenticationRepository
    ) {
        this.auctionRepository = auctionRepository;
        this.authenticationRepository = authenticationRepository;
    }

    public PageResult<Auction> getAllAuctions(
            PageQuery pageQuery
    ) {
        return auctionRepository.findAll(pageQuery);
    }

    public String createAuction(
            Auction auction
    ) {
        User seller = authenticationRepository
                .findByUserId(auction.getSellerId())
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "Seller could not be found in the database"
                        )
                );

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
