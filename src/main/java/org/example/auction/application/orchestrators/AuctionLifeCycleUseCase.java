package org.example.auction.application.orchestrators;


import org.example.auction.domain.entities.Auction;
import org.example.auction.domain.services.AuctionService;
import org.example.shared.data.AuctionState;
import org.example.shared.domain.AuctionEventPublisher;
import org.example.shared.events.AuctionClosedEvent;
import org.example.shared.events.AuctionStartedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class AuctionLifeCycleUseCase {
    private final AuctionEventPublisher auctionEventPublisher;
    private final AuctionService auctionService;

    public AuctionLifeCycleUseCase(
            AuctionEventPublisher auctionEventPublisher,
            AuctionService auctionService
    ) {
        this.auctionEventPublisher = auctionEventPublisher;
        this.auctionService = auctionService;
    }

    @Transactional
    public void startDueActions() {
        List<Auction> due = auctionService
                .getAuctionsByAuctionStateAndStartDateBefore(AuctionState.PENDING, LocalDateTime.now());

        due.forEach(auction -> {
            auctionService.startAuction(auction);
            auctionEventPublisher.publish(
                    new AuctionStartedEvent(
                            auction.getAuctionId()
                    )
            );
        });
    }

    @Transactional
    public void closeExpiredAuctions() {
        List<Auction> expired = auctionService
                .getAuctionsByAuctionStateAndEndDateBefore(AuctionState.ONGOING, LocalDateTime.now());

        expired.forEach(auction -> {
            auctionService.closeAuction(auction);
            auctionEventPublisher.publish(
                    new AuctionClosedEvent(
                            auction.getAuctionId(),
                            auction.getWinnerId(),
                            auction.getSellerId(),
                            auction.getMaxBid()
                    )
            );
        });
    }
}
