package org.example.auction.infrastructure.adapters;

import org.example.auction.domain.entities.Auction;
import org.example.auction.domain.repositories.AuctionRepository;
import org.example.auction.infrastructure.persistance.JpaAuctionRepository;
import org.example.shared.domain.PageQuery;
import org.example.shared.domain.PageResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public class AuctionAdapter implements AuctionRepository {
    private final JpaAuctionRepository jpaAuctionRepository;

    public AuctionAdapter(
            JpaAuctionRepository jpaAuctionRepository
    ) {
        this.jpaAuctionRepository = jpaAuctionRepository;
    }

    private PageResult<Auction> buildAuctionPage(
            Page<Auction> springPage
    ) {
        return new PageResult<>(
                springPage.getContent(),
                springPage.getNumber(),
                springPage.getSize(),
                springPage.getTotalElements(),
                springPage.getTotalPages()
        );
    }

    @Override
    public Optional<Auction> findAuctionByAuctionIdWithLock(Long auctionId) {
        return jpaAuctionRepository.findAuctionByAuctionIdWithLock(auctionId);
    }

    @Override
    public Optional<Auction> findAuctionBySellerIdAndItemName(
            Long seller,
            String item
    ) {
        return jpaAuctionRepository.findAuctionBySellerIdAndItemName(seller, item);
    }

    @Override
    public Optional<Auction> findAuctionByAuctionId(
            Long auctionId
    ) {
        return jpaAuctionRepository.findAuctionByAuctionId(auctionId);
    }

    @Override
    public PageResult<Auction> findAuctionsBySellerId(
            PageQuery pageQuery,
            Long seller
    ) {
        PageRequest springPageRequest = PageRequest.of(pageQuery.pageNumber(), pageQuery.pageSize());
        Page<Auction> springPage = jpaAuctionRepository.findAuctionsBySellerId(springPageRequest, seller);
        return buildAuctionPage(springPage);
    }

    @Override
    public PageResult<Auction> findAuctionsByWinnerId(
            PageQuery pageQuery,
            Long winner
    ) {
        PageRequest springPageRequest = PageRequest.of(pageQuery.pageNumber(), pageQuery.pageSize());
        Page<Auction> springPage = jpaAuctionRepository.findAuctionsByWinnerId(springPageRequest, winner);
        return buildAuctionPage(springPage);
    }

    @Override
    public PageResult<Auction> findAll(
            PageQuery pageQuery
    ) {
        PageRequest springPageRequest = PageRequest.of(pageQuery.pageNumber(), pageQuery.pageSize());
        Page<Auction> springPage = jpaAuctionRepository.findAll(springPageRequest);
        return buildAuctionPage(springPage);
    }

    @Override
    public Auction save(
            Auction auction
    ) {
        return jpaAuctionRepository.save(auction);
    }
}
