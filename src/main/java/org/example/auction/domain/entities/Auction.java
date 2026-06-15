package org.example.auction.domain.entities;


import jakarta.persistence.*;
import lombok.Data;
import org.example.auction.domain.data.AuctionState;
import org.example.user.domain.entities.User;

import java.time.LocalDate;


@Data
@Entity
@Table(
        name = "auction",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_auction_seller_item",
                        columnNames = {"seller_id", "item_name"}
                )
        }
)
public class Auction {

    @Id
    @Column(name = "auction_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long auctionId;

    @Column(name = "item_name", nullable = false)
    private String itemName;

    @Column(name = "item_description")
    private String itemDescription;

    @Column(name = "starting_price", nullable = false)
    private Double startingPrice = 0.0;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "max_bid", nullable = false)
    private Double maxBid;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @ManyToOne
    @JoinColumn(name = "winner_id", nullable = false)
    private User winner;

    @Enumerated(EnumType.STRING)
    @Column(name = "auction_state", nullable = false)
    private AuctionState auctionState = AuctionState.NOT_STARTED;
}
