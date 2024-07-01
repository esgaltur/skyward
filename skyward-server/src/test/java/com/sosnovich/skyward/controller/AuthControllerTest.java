package com.sosnovich.skyward.controller;

import com.sosnovich.skyward.openapi.model.AuthResponse;
import com.sosnovich.skyward.openapi.model.Credentials;
import com.sosnovich.skyward.security.api.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import jakarta.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthenticateUser_Success() {
        // Arrange
        Credentials credentials = new Credentials();
        credentials.setEmail("user@example.com");
        credentials.setPassword("password");

        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(tokenService.createToken(authentication)).thenReturn("test-token");

        // Act
        Response response = authController.authenticateUser(credentials);

        // Assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        AuthResponse authResponse = (AuthResponse) response.getEntity();
        assertEquals("test-token", authResponse.getToken());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenService, times(1)).createToken(authentication);
    }

    @Test
    void testAuthenticateUser_BadCredentials() {
        // Arrange
        Credentials credentials = new Credentials();
        credentials.setEmail("user@example.com");
        credentials.setPassword("wrong-password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> authController.authenticateUser(credentials));

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenService, times(0)).createToken(any(Authentication.class));
    }
}
