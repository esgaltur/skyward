package com.sosnovich.skyward.security.api;

import org.springframework.security.core.Authentication;

/**
 * The TokenService interface provides methods for creating authentication tokens.
 */
public interface TokenService {

    /**
     * Creates a token based on the given authentication details.
     *
     * @param authentication the authentication details used to create the token
     * @return the generated token as a String
     */
    String createToken(Authentication authentication);
}
