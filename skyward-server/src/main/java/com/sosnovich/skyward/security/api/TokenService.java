package com.sosnovich.skyward.security.api;

import org.springframework.security.core.Authentication;

public interface TokenService {

    String createToken(Authentication authentication);
}
