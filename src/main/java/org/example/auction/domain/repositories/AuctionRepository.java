package org.example.auction.domain.repositories;


import org.example.auction.domain.entities.Auction;
import org.example.shared.data.AuctionState;
import org.example.shared.domain.PageQuery;
import org.example.shared.domain.PageResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface AuctionRepository {
    Optional<Auction> findAuctionByAuctionIdWithLock(Long auctionId);
    Optional<Auction> findAuctionBySellerIdAndItemName(Long seller, String item);
    Optional<Auction> findAuctionByAuctionId(Long auctionId);
    PageResult<Auction> findAuctionsBySellerId(PageQuery pageQuery, Long seller);
    PageResult<Auction> findAuctionsByWinnerId(PageQuery pageQuery, Long winner);
    PageResult<Auction> findAll(PageQuery pageQuery);
    List<Auction> findAuctionsByAuctionStateAndStartDateBefore(AuctionState state, LocalDateTime threshold);
    List<Auction> findAuctionsByAuctionStateAndEndDateBefore(AuctionState state, LocalDateTime threshold);
    Auction save(Auction auction);
}
