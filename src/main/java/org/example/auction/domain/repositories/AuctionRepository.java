package org.example.auction.domain.repositories;


import org.example.auction.domain.entities.Auction;
import org.example.user.domain.entities.User;

import java.util.List;


public interface AuctionRepository {
    Auction findAuctionBySellerAndItem(User seller, String item);
    List<Auction> findAuctionsByUser(User user);
    List<Auction> findAllAuctions();
    Auction save(Auction auction);
}
