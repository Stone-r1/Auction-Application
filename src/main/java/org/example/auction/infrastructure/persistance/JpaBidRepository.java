package org.example.auction.infrastructure.persistance;


import jakarta.persistence.LockModeType;
import org.example.auction.domain.entities.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface JpaBidRepository extends JpaRepository<Bid, Long> {
    // Acquires an exclusive lock on the row, preventing other transactions from reading or
    // writing to it until your transaction finishes.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM Bid b WHERE b.bidId = :id")
    Optional<Bid> findBidByBidIdWithLock(@Param("id") Long id);

    Optional<Bid> findTopByAuctionIdOrderByAmountDesc(Long auctionId);
    Bid save(Bid bid);
}
