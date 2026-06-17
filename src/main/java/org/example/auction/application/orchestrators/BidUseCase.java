package org.example.auction.application.orchestrators;

import org.example.auction.application.dto.PlaceBidRequest;
import org.example.auction.domain.entities.Bid;
import org.example.auction.domain.services.BidService;
import org.example.shared.domain.AuctionEventPublisher;
import org.example.shared.events.BidPlacedEvent;
import org.example.user.domain.repositories.CurrentUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
public class BidUseCase {
    private final BidService bidService;
    private final CurrentUserRepository currentUserRepository;
    private final AuctionEventPublisher auctionEventPublisher;


    public BidUseCase(
            BidService bidService,
            CurrentUserRepository currentUserRepository,
            AuctionEventPublisher auctionEventPublisher
    ) {
        this.bidService = bidService;
        this.currentUserRepository = currentUserRepository;
        this.auctionEventPublisher = auctionEventPublisher;
    }

    @Transactional
    public String placeBid(
            Long auctionId,
            PlaceBidRequest placeBidRequest
    ) {
        Long bidderId = currentUserRepository.getCurrentUser().getUserId();

        Bid bid = new Bid();
        bid.setAuctionId(auctionId);
        bid.setUserId(bidderId);
        bid.setAmount(placeBidRequest.amount());
        bid.setPlacedAt(LocalDateTime.now());

        // arguable, but placing bid and returning previous winner's ID reduces DB calls
        Long previousWinner = bidService.placeBid(bid);

        // notify previous max bid owner that they were outbid
        if (previousWinner != null) {
            auctionEventPublisher
                    .publish(new BidPlacedEvent(
                            auctionId,
                            bidderId,
                            placeBidRequest.amount(),
                            previousWinner
                    ));
        }

        return "Bid was placed successfully!";
    }
}
