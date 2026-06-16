package org.example.auction.infrastructure.configs;

import org.example.auction.domain.repositories.AuctionRepository;
import org.example.auction.domain.services.AuctionService;
import org.example.user.domain.repositories.AuthenticationRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AuctionConfig {

    @Bean
    public AuctionService auctionService(
            AuctionRepository auctionRepository,
            AuthenticationRepository authenticationRepository
    ) {
        return new AuctionService(auctionRepository, authenticationRepository);
    }
}
