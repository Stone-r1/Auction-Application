package org.example.auction.application;


import org.example.auction.domain.entities.Auction;
import org.example.auction.domain.services.AuctionService;
import org.example.shared.domain.PageQuery;
import org.example.shared.domain.PageResult;
import org.springframework.stereotype.Service;


@Service
public class AuctionUseCase {
    private final AuctionService auctionService;

    public AuctionUseCase(
            AuctionService auctionService
    ) {
        this.auctionService = auctionService;
    }

    public PageResult<Auction> getAvailableAuctions(
            int pageNumber,
            int pageSize
    ) {
        return auctionService.getAllAuctions(new PageQuery(pageNumber, pageSize));
    }
}
