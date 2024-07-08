package com.live.auction.dto;

import com.live.auction.common.AuctionStatus;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuctionHistoryDto {

    private Long itemId;

    private BigDecimal basePrice;

    private Instant startDate;

    private Instant endDate;

    private BigDecimal highestPrice;

    private AuctionStatus status;
}
