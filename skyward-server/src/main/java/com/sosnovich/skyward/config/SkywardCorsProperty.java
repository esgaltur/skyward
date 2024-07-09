package com.sosnovich.skyward.config;



import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.List;

/**
 *
 * @param allowCredentials {@link org.springframework.web.cors.CorsConfiguration#setAllowCredentials(Boolean)}
 * @param allowedOrigins {@link org.springframework.web.cors.CorsConfiguration#setAllowedOrigins(List)}
 * @param allowedHeaders {@link org.springframework.web.cors.CorsConfiguration#setAllowedHeaders(List)}
 * @param allowedMethods {@link org.springframework.web.cors.CorsConfiguration#setAllowCredentials(Boolean)
 * @param maxAge {@link org.springframework.web.cors.CorsConfiguration#setMaxAge(Long)}
 */
@ConfigurationProperties(prefix = "skyward.cors")
public record SkywardCorsProperty(
        @DefaultValue("true") boolean allowCredentials,
        @DefaultValue("*") List<String> allowedOrigins,
        @DefaultValue("*") List<String> allowedHeaders,
        @DefaultValue({"GET", "POST", "PUT", "DELETE", "OPTIONS"}) List<String> allowedMethods,
        @DefaultValue("3600") Long maxAge
) {}

