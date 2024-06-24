package com.sosnovich.skyward.security;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderImplTest {

    @Test
    void init() {
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());
        System.out.println(base64Key);
    }

    @Test
    void resolveToken() {
    }

    @Test
    void createToken() {
    }

    @Test
    void getUsername() {
    }

    @Test
    void validateToken() {
    }

    @Test
    void parser() {
    }
}