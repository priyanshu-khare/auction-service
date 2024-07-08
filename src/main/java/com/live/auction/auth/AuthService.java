package com.live.auction.auth;

import com.live.auction.util.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final JwtUtil jwtUtil;

    public AuthService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public boolean validateRole(String token, String role) {
        token = token.substring(7);
        if (jwtUtil.isTokenExpired(token)) {
            return false;
        }
        return jwtUtil.getRole(token).equals(role);
    }

    public Long getUserId(String token) {
        token = token.substring(7);
        return Long.parseLong(jwtUtil.getSubject(token));
    }
}
