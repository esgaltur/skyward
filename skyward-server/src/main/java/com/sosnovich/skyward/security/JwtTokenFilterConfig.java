package com.sosnovich.skyward.security;

import com.sosnovich.skyward.security.api.JwtTokenProvider;
import com.sosnovich.skyward.security.filter.JwtTokenJerseyFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class JwtTokenFilterConfig {

    /**
     * Configures the JWT Token Filter for API security.
     *
     * @param jwtTokenProvider   provides JWT tokens
     * @param userDetailsService loads user-specific data
     * @return a FilterRegistrationBean for the JWT Token Filter
     */
    @Bean
    public FilterRegistrationBean<JwtTokenJerseyFilter> jwtTokenFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        FilterRegistrationBean<JwtTokenJerseyFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtTokenJerseyFilter(jwtTokenProvider, userDetailsService));
        registrationBean.addUrlPatterns("/api/**"); // Adjust the URL pattern to match your API endpoints
        return registrationBean;
    }
}
