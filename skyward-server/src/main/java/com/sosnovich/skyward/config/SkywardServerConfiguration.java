package com.sosnovich.skyward.config;

import com.sosnovich.skyward.data.config.JpaConfig;
import com.sosnovich.skyward.security.filter.JwtTokenJerseyFilter;
import com.sosnovich.skyward.security.SecurityConfig;
import com.sosnovich.skyward.security.api.JwtTokenProvider;
import com.sosnovich.skyward.tracing.OpenTelemetryConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.userdetails.UserDetailsService;

import static io.undertow.servlet.Servlets.servlet;

/**
 * SkywardServerConfiguration class configures the server and various security
 * aspects for the application.
 */
@Configuration
@Import({OpenTelemetryConfig.class, SecurityConfig.class, JpaConfig.class})
@EnableAutoConfiguration
@ComponentScan(basePackageClasses = SkywardServerConfiguration.class)
@EnableAsync
public class SkywardServerConfiguration {

    /**
     * Configures the JWT Token Filter for API security.
     *
     * @param jwtTokenProvider provides JWT tokens
     * @param userDetailsService loads user-specific data
     * @return a FilterRegistrationBean for the JWT Token Filter
     */
    @Bean
    public FilterRegistrationBean<JwtTokenJerseyFilter> jwtTokenFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        FilterRegistrationBean<JwtTokenJerseyFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtTokenJerseyFilter(jwtTokenProvider, userDetailsService));
        registrationBean.addUrlPatterns("/api/*"); // Adjust the URL pattern to match your API endpoints
        return registrationBean;
    }

    /**
     * Configures the Undertow Servlet Web Server Factory.
     *
     * @return an UndertowServletWebServerFactory instance
     */
    @Bean
    public UndertowServletWebServerFactory undertowServletWebServerFactory() {
        UndertowServletWebServerFactory factory = new UndertowServletWebServerFactory();
        factory.addDeploymentInfoCustomizers(deploymentInfo -> {
            deploymentInfo.addInitParameter("jakarta.ws.rs.Application", JerseyConfig.class.getName());
            deploymentInfo.addServlets(servlet("JerseyServlet", ServletContainer.class));
        });
        return factory;
    }
}
