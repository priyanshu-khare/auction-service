package com.live.auction.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "auction")
public class Auction {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "base_price")
    private BigDecimal basePrice;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Column(name = "highest_price")
    private BigDecimal highestPrice;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "status")
    private String status;

    @UpdateTimestamp
    @Column(name = "updated_on")
    private Instant updatedOn;
}
