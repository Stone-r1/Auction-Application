package org.example.auction.domain.repositories;


import org.example.auction.domain.entities.Bid;

public interface BidRepository {
    Bid save(Bid bid);
}
