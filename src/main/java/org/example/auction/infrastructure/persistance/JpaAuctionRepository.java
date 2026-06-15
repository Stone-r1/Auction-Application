package org.example.auction.infrastructure.persistance;

import jakarta.validation.constraints.NotNull;
import org.example.auction.domain.entities.Auction;
import org.example.user.domain.entities.User;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface JpaAuctionRepository extends JpaRepository<Auction, Long> {
    Optional<Auction> findAuctionBySellerAndItemName(User seller, String item);
    Page<Auction> findAuctionsBySeller(@NotNull Pageable pageable, User seller);
    Page<Auction> findAuctionsByWinner(@NotNull Pageable pageable, User winner);
    Page<Auction> findAll(@NonNull Pageable pageable);
    Auction save(Auction auction);
}
