package com.sosnovich.skyward.config;

import com.sosnovich.skyward.controller.AuthController;
import com.sosnovich.skyward.controller.UserController;
import com.sosnovich.skyward.exceptionmappers.*;
import com.sosnovich.skyward.tracing.filters.TraceFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.context.annotation.Configuration;

/**
 * JerseyConfig class configures the Jersey framework for the application.
 * It registers various controllers, exception mappers, and features
 * necessary for the RESTful API.
 */
@Configuration
public class JerseyConfig extends ResourceConfig {

    /**
     * Constructor to configure Jersey with specific settings and register
     * various components like controllers, filters, and exception mappers.
     */
    public JerseyConfig() {
        // Register controllers
        register(AuthController.class);
        register(UserController.class);
        // Register features
        register(JacksonFeature.class);
        // Register filters
        register(TraceFilter.class);
        // Register exception mappers
        registerExceptionMappers();
        // Set various properties
        registerConfiguration();
    }

    private void registerConfiguration() {
        this.property(ServerProperties.WADL_FEATURE_DISABLE, true);
        this.property(ServerProperties.RESPONSE_SET_STATUS_OVER_SEND_ERROR, true);
        this.property(ServerProperties.TRACING, "ON_DEMAND");
        this.property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
        this.property(ServletProperties.FILTER_CONTEXT_PATH, "/");
        this.property(ServletProperties.FILTER_FORWARD_ON_404, true);
    }

    private void registerExceptionMappers() {
        register(MultithreadExceptionExceptionMapper.class);
        register(UsernameNotFoundExceptionExceptionMapper.class);
        register(NotFoundExceptionExceptionMapper.class);
        register(EmailAlreadyInUseExceptionMapper.class);
        register(AuthorizationDeniedExceptionExceptionMapper.class);
        register(ForbiddenExceptionMapper.class);
        register(ConstraintViolationExceptionMapper.class);
        register(BadCredentialsExceptionMapper.class);
        register(UserNotFoundExceptionMapper.class);
        register(ProjectAlreadyExistsExceptionMapper.class);
        register(PathParamExceptionMapper.class);
        register(ExceptionExceptionMapper.class);
    }
}
