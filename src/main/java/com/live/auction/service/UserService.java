package com.live.auction.service;

import com.live.auction.dto.UserAuthDto;
import com.live.auction.dto.UserDto;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    Optional<UserAuthDto> login(UserDto userDto) throws Exception;

    void register(UserDto userDto);
}
