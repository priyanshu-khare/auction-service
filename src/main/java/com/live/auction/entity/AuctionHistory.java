package com.live.auction.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "auction_history")
public class AuctionHistory {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "auction_id")
    private Long auctionId;

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "highest_price")
    private BigDecimal highestPrice;

    @Column(name = "base_price")
    private BigDecimal basePrice;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "status")
    private String status;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @CreationTimestamp
    @Column(name = "created_on")
    private Instant createdOn;
}
