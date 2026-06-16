package org.example.auction.infrastructure.persistance;

import jakarta.persistence.LockModeType;
import jakarta.validation.constraints.NotNull;
import org.example.auction.domain.entities.Auction;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface JpaAuctionRepository extends JpaRepository<Auction, Long> {
    // Acquires an exclusive lock on the row, preventing other transactions from reading or
    // writing to it until your transaction finishes.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Auction a WHERE a.auctionId = :id")
    Optional<Auction> findAuctionByAuctionIdWithLock(@Param("id") Long id);

    Optional<Auction> findAuctionBySellerIdAndItemName(Long sellerId, String item);
    Optional<Auction> findAuctionByAuctionId(Long auctionId);
    Page<Auction> findAuctionsBySellerId(@NotNull Pageable pageable, Long seller);
    Page<Auction> findAuctionsByWinnerId(@NotNull Pageable pageable, Long winner);
    Page<Auction> findAll(@NonNull Pageable pageable);
    Auction save(Auction auction);
}
