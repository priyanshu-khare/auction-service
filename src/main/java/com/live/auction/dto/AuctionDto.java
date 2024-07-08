package com.live.auction.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuctionDto {

    private long itemId;

    private BigDecimal highestPrice;

    private long updatedBy;

    private String status;
}
