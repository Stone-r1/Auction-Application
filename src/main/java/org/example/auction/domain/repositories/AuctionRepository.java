package org.example.auction.domain.repositories;


import org.example.auction.domain.entities.Auction;
import org.example.shared.domain.PageQuery;
import org.example.shared.domain.PageResult;
import org.example.user.domain.entities.User;

import java.util.List;
import java.util.Optional;


public interface AuctionRepository {
    Optional<Auction> findAuctionBySellerAndItem(User seller, String item);
    PageResult<Auction> findAuctionsByUser(PageQuery pageQuery, User user);
    PageResult<Auction> findAll(PageQuery pageQuery);
    Auction save(Auction auction);
}
