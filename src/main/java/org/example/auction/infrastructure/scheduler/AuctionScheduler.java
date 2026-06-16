package org.example.auction.infrastructure.scheduler;


import org.example.auction.application.orchestrators.AuctionLifeCycleUseCase;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@EnableScheduling
public class AuctionScheduler {
    private final AuctionLifeCycleUseCase auctionLifeCycleUseCase;

    public AuctionScheduler(
            AuctionLifeCycleUseCase auctionLifeCycleUseCase
    ) {
        this.auctionLifeCycleUseCase = auctionLifeCycleUseCase;
    }

    // every 60 seconds
    @Scheduled(fixedDelay = 60_000)
    public void startPendingAuctions() {
        auctionLifeCycleUseCase.startDueActions();
    }

    @Scheduled(fixedDelay = 60_000)
    public void closeExpiredAuctions() {
        auctionLifeCycleUseCase.closeExpiredAuctions();
    }
}
