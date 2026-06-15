package org.example.auction.infrastructure.persistance;

import jakarta.validation.constraints.NotNull;
import org.example.auction.domain.entities.Auction;
import org.example.user.domain.entities.User;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface JpaAuctionRepository extends JpaRepository<Auction, Long> {
    Optional<Auction> findAuctionBySellerAndItem(User seller, String item);
    Page<Auction> findAuctionsByUser(@NotNull Pageable pageable, User user);
    Page<Auction> findAll(@NonNull Pageable pageable);
    Auction save(Auction auction);
}
