package com.live.auction.dto;

import jakarta.validation.constraints.NotNull;
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
public class BidDto {

    @NotNull private Long itemId;

    @NotNull private BigDecimal price;

    @NotNull private BigDecimal basePrice;

    @NotNull private Instant startDate;

    @NotNull private Instant endDate;
}
