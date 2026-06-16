package org.example.auction.domain.repositories;


import org.example.auction.domain.entities.Auction;
import org.example.shared.domain.PageQuery;
import org.example.shared.domain.PageResult;

import java.util.Optional;


public interface AuctionRepository {
    Optional<Auction> findAuctionBySellerIdAndItemName(Long seller, String item);
    PageResult<Auction> findAuctionsBySellerId(PageQuery pageQuery, Long seller);
    PageResult<Auction> findAuctionsByWinnerId(PageQuery pageQuery, Long winner);
    PageResult<Auction> findAll(PageQuery pageQuery);
    Auction save(Auction auction);
}
