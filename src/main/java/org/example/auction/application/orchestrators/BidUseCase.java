package org.example.auction.application.orchestrators;

import org.example.auction.application.dto.PlaceBidRequest;
import org.example.auction.domain.entities.Bid;
import org.example.auction.domain.services.BidService;
import org.example.user.domain.repositories.CurrentUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
public class BidUseCase {
    private final BidService bidService;
    private final CurrentUserRepository currentUserRepository;

    public BidUseCase(
            BidService bidService,
            CurrentUserRepository currentUserRepository
    ) {
        this.bidService = bidService;
        this.currentUserRepository = currentUserRepository;
    }

    @Transactional
    public Bid placeBid(
            Long auctionId,
            PlaceBidRequest placeBidRequest
    ) {
        Bid bid = new Bid();
        bid.setAuctionId(auctionId);
        bid.setUserId(currentUserRepository.getCurrentUser().getUserId());
        bid.setAmount(placeBidRequest.amount());
        bid.setPlacedAt(LocalDateTime.now());

        return bidService.placeBid(bid);
    }
}
