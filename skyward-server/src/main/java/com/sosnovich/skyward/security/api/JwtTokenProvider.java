package com.sosnovich.skyward.security.api;

/**
 * The JwtTokenProvider interface provides methods for creating and validating JWT tokens.
 */
public interface JwtTokenProvider {

    /**
     * Creates a JWT token based on the given username, user ID, and role.
     *
     * @param username the username for which the token is created
     * @param userId the user ID for which the token is created
     * @param role the role of the user for which the token is created
     * @return the generated JWT token as a String
     */
    String createToken(String username, Long userId, String role);

    /**
     * Retrieves the username from the given JWT token.
     *
     * @param token the JWT token
     * @return the username extracted from the token
     */
    String getUsername(String token);

    /**
     * Validates the given JWT token.
     *
     * @param token the JWT token to be validated
     * @return true if the token is valid, false otherwise
     */
    boolean validateToken(String token);

    /**
     * Retrieves the role from the given JWT token.
     *
     * @param token the JWT token
     * @return the role extracted from the token
     */
    String getRole(String token);
}
