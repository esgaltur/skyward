package com.sosnovich.skyward.controller;

import com.sosnovich.skyward.openapi.model.AuthResponse;
import com.sosnovich.skyward.openapi.api.AuthApi;
import com.sosnovich.skyward.openapi.model.Credentials;
import com.sosnovich.skyward.security.api.TokenService;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * AuthController handles authentication requests and issues JWT tokens.
 */
@Component
public class AuthController implements AuthApi {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    /**
     * Constructs an AuthController with the specified AuthenticationManager and JwtTokenProvider.
     *
     * @param authenticationManager the AuthenticationManager to authenticate user credentials
     * @param tokenService          the JwtTokenProvider to generate JWT tokens
     */
    @Autowired
    public AuthController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    /**
     * Authenticates a user and generates a JWT token if the authentication is successful.
     *
     * @param credentials the user's credentials (email and password)
     * @return a Response containing the JWT token and a trace ID
     */
    @Override
    public Response authenticateUser(Credentials credentials) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(credentials.getEmail(), credentials.getPassword())
        );
        final String token = tokenService.createToken(authentication);
        return Response.ok(new AuthResponse(token)).build();
    }
}
