package org.example.auction.infrastructure.adapters;


import org.example.auction.domain.entities.Bid;
import org.example.auction.domain.repositories.BidRepository;
import org.example.auction.infrastructure.persistance.JpaBidRepository;
import org.springframework.stereotype.Repository;


@Repository
public class BidAdapter implements BidRepository {
    public final JpaBidRepository jpaBidRepository;

    public BidAdapter(
            JpaBidRepository jpaBidRepository
    ) {
        this.jpaBidRepository = jpaBidRepository;
    }

    @Override
    public Bid save(
            Bid bid
    ) {
        return jpaBidRepository.save(bid);
    }
}
