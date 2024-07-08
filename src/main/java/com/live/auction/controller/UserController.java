package com.live.auction.controller;

import com.live.auction.dto.UserAuthDto;
import com.live.auction.dto.UserDto;
import com.live.auction.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Tag(name = "User")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register", description = "A new user registration")
    public ResponseEntity<Void> register(@RequestBody UserDto userDto) {
        userService.register(userDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "User login")
    public ResponseEntity<UserAuthDto> login(@RequestBody UserDto userDto) throws Exception {
        Optional<UserAuthDto> authDtoOptional = userService.login(userDto);
        return authDtoOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound()
                .build());
    }
}
