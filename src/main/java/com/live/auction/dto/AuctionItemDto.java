package com.live.auction.dto;

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
public class AuctionItemDto {

    private Long itemId;

    private Instant startDate;

    private Instant endDate;

    private BigDecimal basePrice;

    private BigDecimal highestPrice;
}
