package com.live.auction.dto;

import com.live.auction.common.UserType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDto {

    @NotNull private UserType type;

    @NotNull private String userId;

    @NotNull private String password;

    @NotNull private String name;
}
