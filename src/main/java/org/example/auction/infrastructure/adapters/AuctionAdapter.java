package org.example.auction.infrastructure.adapters;

import org.example.AuctionApplication;
import org.example.auction.domain.entities.Auction;
import org.example.auction.domain.repositories.AuctionRepository;
import org.example.auction.infrastructure.persistance.JpaAuctionRepository;
import org.example.user.domain.entities.User;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class AuctionAdapter implements AuctionRepository {
    private final JpaAuctionRepository jpaAuctionRepository;

    public AuctionAdapter(
            JpaAuctionRepository jpaAuctionRepository
    ) {
        this.jpaAuctionRepository = jpaAuctionRepository;
    }

    @Override
    public Auction findAuctionBySellerAndItem(User seller, String item) {
        return null;
    }

    @Override
    public List<Auction> findAuctionsByUser(User user) {
        return List.of();
    }

    @Override
    public List<Auction> findAllAuctions() {
        return jpaAuctionRepository.findAllAuctions();
    }

    @Override
    public Auction save(Auction auction) {
        return null;
    }
}
