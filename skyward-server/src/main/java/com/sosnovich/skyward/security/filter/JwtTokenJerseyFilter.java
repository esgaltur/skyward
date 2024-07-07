package com.sosnovich.skyward.security.filter;

import com.sosnovich.skyward.security.api.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.io.IOException;

/**
 * Filter for JWT token authentication in Jersey-based applications.
 */
public class JwtTokenJerseyFilter implements jakarta.servlet.Filter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    /**
     * Constructs a JwtTokenJerseyFilter with the specified JwtTokenProvider and UserDetailsService.
     *
     * @param jwtTokenProvider the provider for JWT tokens
     * @param userDetailsService the service for loading user-specific data
     */
    public JwtTokenJerseyFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Resolves the JWT token from the request header.
     *
     * @param request the HttpServletRequest
     * @return the JWT token or null if the token is not found
     */
    private String resolveToken(HttpServletRequest request) {
        // The length of the "Bearer " prefix in the Authorization header
        final int BEARER_PREFIX_LENGTH = 7;

        String bearerToken = request.getHeader("Authorization");

        // Check if the Authorization header contains a Bearer token
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            // Extract and return the JWT token by removing the "Bearer " prefix
            return bearerToken.substring(BEARER_PREFIX_LENGTH);
        }

        // Return null if the token is not found or does not start with "Bearer "
        return null;
    }

    /**
     * Filters incoming requests for JWT token authentication.
     *
     * @param request the ServletRequest
     * @param response the ServletResponse
     * @param chain the FilterChain
     * @throws IOException if an input or output exception occurs
     * @throws ServletException if a servlet exception occurs
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String token = resolveToken(httpRequest);
        if (token != null && jwtTokenProvider.validateToken(token)) {
            String username = jwtTokenProvider.getUsername(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (userDetails != null) {
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        chain.doFilter(httpRequest, httpResponse);
    }
}
