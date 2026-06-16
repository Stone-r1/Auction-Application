package org.example.auction.infrastructure.persistance;

import jakarta.validation.constraints.NotNull;
import org.example.auction.domain.entities.Auction;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface JpaAuctionRepository extends JpaRepository<Auction, Long> {
    Optional<Auction> findAuctionBySellerIdAndItemName(Long sellerId, String item);
    Page<Auction> findAuctionsBySellerId(@NotNull Pageable pageable, Long seller);
    Page<Auction> findAuctionsByWinnerId(@NotNull Pageable pageable, Long winner);
    Page<Auction> findAll(@NonNull Pageable pageable);
    Auction save(Auction auction);
}
