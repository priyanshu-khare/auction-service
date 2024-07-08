package com.live.auction.dto;

import com.live.auction.common.AuctionStatus;
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

    private Long itemId;

    private BigDecimal highestPrice;

    private Long updatedBy;

    private AuctionStatus status;
}
