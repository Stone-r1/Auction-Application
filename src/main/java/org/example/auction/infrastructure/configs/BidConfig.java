package org.example.auction.infrastructure.configs;


import org.example.auction.domain.repositories.AuctionRepository;
import org.example.auction.domain.repositories.BidRepository;
import org.example.auction.domain.services.BidService;
import org.example.user.domain.repositories.AuthenticationRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class BidConfig {

    @Bean
    public BidService bidService(
            BidRepository bidRepository,
            AuthenticationRepository authenticationRepository,
            AuctionRepository auctionRepository
    ) {
        return new BidService(bidRepository, authenticationRepository, auctionRepository);
    }
}
