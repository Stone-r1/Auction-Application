package org.example.auction.domain.repositories;


import org.example.auction.domain.entities.Bid;

import java.util.Optional;

public interface BidRepository {
    Optional<Bid> findBidByBidIdWithLock(Long id);
    Optional<Bid> findTopByAuctionIdOrderByAmountDesc(Long auctionId);
    Bid save(Bid bid);
}
