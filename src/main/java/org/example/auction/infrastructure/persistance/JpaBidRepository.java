package org.example.auction.infrastructure.persistance;


import org.example.auction.domain.entities.Bid;
import org.springframework.data.jpa.repository.JpaRepository;


public interface JpaBidRepository extends JpaRepository<Bid, Long> {
    Bid save(Bid bid);
}
