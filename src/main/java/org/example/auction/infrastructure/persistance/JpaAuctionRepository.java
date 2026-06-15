package org.example.auction.infrastructure.persistance;

import org.example.auction.domain.entities.Auction;
import org.example.user.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface JpaAuctionRepository extends JpaRepository<Auction, Long> {
    Auction findAuctionBySellerAndItem(User seller, String item);
    List<Auction> findAuctionsByUser(User user);
    List<Auction> findAllAuctions();
    Auction save(Auction auction);
}
