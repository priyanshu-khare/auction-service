package com.live.auction.service.impl;

import com.live.auction.dto.UserAuthDto;
import com.live.auction.dto.UserDto;
import com.live.auction.entity.User;
import com.live.auction.repository.UserRepository;
import com.live.auction.service.UserService;
import com.live.auction.util.JwtUtil;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final ModelMapper mapper;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository, ModelMapper mapper, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    @Transactional
    public Optional<UserAuthDto> login(UserDto userDto) throws Exception {
        User user = userRepository.findByUserId(userDto.getUserId());
        if (user != null && passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            String jwt = jwtUtil.generateToken(String.valueOf(userDto.getUserId()), String.valueOf(user.getType()));
            return Optional.of(UserAuthDto.builder().token(jwt).build());
        }
        throw new Exception("Invalid credentials");
    }

    @Override
    @Transactional
    public void register(UserDto userDto) {
        User user = mapper.map(userDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setId(null);
        userRepository.save(user);
    }
}
