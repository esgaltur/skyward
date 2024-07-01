package com.sosnovich.skyward.security;

import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderImplTest {

    private JwtTokenProviderImpl jwtTokenProvider;

    @Value("${security.jwt.token.secret-key}")
    private String secretKey = "my-secret-key-my-secret-key-my-secret-key"; // Example key, should be encoded

    @Value("${security.jwt.token.expire-length:3600000}") // 1 hour
    private long validityInMilliseconds = 3600000L; // Setting it to 1 hour for the tests

    private SecretKey key;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProviderImpl();
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", Base64.getEncoder().encodeToString(secretKey.getBytes()));
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", validityInMilliseconds);
        jwtTokenProvider.init();
        key = (SecretKey) ReflectionTestUtils.getField(jwtTokenProvider, "key");
    }

    @Test
    void testCreateToken() {
        String token = jwtTokenProvider.createToken("user@example.com", 1L, "ROLE_USER");
        assertNotNull(token);

        var claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        assertEquals("user@example.com", claims.getSubject());
        assertEquals("ROLE_USER", claims.get("role"));
        assertEquals(1, claims.get("userId"));
    }

    @Test
    void testGetUsername() {
        String token = jwtTokenProvider.createToken("user@example.com", 1L, "ROLE_USER");
        String username = jwtTokenProvider.getUsername(token);
        assertEquals("user@example.com", username);
    }

    @Test
    void testValidateToken_Valid() {
        String token = jwtTokenProvider.createToken("user@example.com", 1L, "ROLE_USER");
        boolean isValid = jwtTokenProvider.validateToken(token);
        assertTrue(isValid);
    }

    @Test
    void testValidateToken_Invalid() {
        String token = jwtTokenProvider.createToken("user@example.com", 1L, "ROLE_USER");
        // Alter the token to make it invalid
        token = token.replace(token.charAt(token.length() - 1), 'X');
        boolean isValid = jwtTokenProvider.validateToken(token);
        assertFalse(isValid);
    }

    @Test
    void testGetRole() {
        String token = jwtTokenProvider.createToken("user@example.com", 1L, "ROLE_USER");
        String role = jwtTokenProvider.getRole(token);
        assertEquals("ROLE_USER", role);
    }
}
