package com.live.auction.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
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
public class ItemDto {

    @NotEmpty
    private String name;

    private Instant startDate;

    @Future
    private Instant endDate;

    private String createdBy;

    private BigDecimal basePrice;
}
