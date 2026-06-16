package org.example.auction.application;


import org.example.auction.domain.entities.Auction;
import org.example.auction.domain.services.AuctionService;
import org.example.shared.domain.PageQuery;
import org.example.shared.domain.PageResult;
import org.example.user.domain.repositories.CurrentUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class AuctionUseCase {
    private final AuctionService auctionService;
    private final CurrentUserRepository currentUserRepository;

    public AuctionUseCase(
            AuctionService auctionService,
            CurrentUserRepository currentUserRepository
    ) {
        this.auctionService = auctionService;
        this.currentUserRepository = currentUserRepository;
    }

    @Transactional(readOnly = true)
    public PageResult<Auction> getAvailableAuctions(
            int pageNumber,
            int pageSize
    ) {
        return auctionService.getAllAuctions(new PageQuery(pageNumber, pageSize));
    }

    @Transactional
    public String createAuction(
            CreateAuctionRequest createAuctionRequest
    ) {
        Auction auction = new Auction();
        auction.setSellerId(currentUserRepository.getCurrentUser().getUserId());
        auction.setItemName(createAuctionRequest.itemName());
        auction.setItemDescription(createAuctionRequest.itemDescription());
        auction.setStartingPrice(createAuctionRequest.startingPrice());
        auction.setStartDate(createAuctionRequest.startDate());
        auction.setEndDate(createAuctionRequest.startDate().plusHours(createAuctionRequest.duration()));

        return auctionService.createAuction(auction);
    }
}
