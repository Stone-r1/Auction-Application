package org.example.auction.domain.entities;


import jakarta.persistence.*;
import lombok.Data;
import org.example.user.domain.entities.User;

import java.time.LocalDate;


@Data
@Entity
@Table(name = "bid")
public class Bid {

    @Id
    @Column(name = "bid_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bidId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "auction_id", nullable = false)
    private Auction auction;

    @Column(nullable = false)
    private Double amount;

    @Column(name = "placed_at", nullable = false)
    private LocalDate placedAt = LocalDate.now();
}
