package com.sosnovich.skyward.security.api;

public interface JwtTokenProvider {
    String createToken(String username, Long userId, String role);

    String getUsername(String token);

    boolean validateToken(String token);

    String getRole(String token);
}
