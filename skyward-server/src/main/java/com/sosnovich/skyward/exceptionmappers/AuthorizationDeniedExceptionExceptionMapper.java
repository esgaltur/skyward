package com.sosnovich.skyward.exceptionmappers;

import com.sosnovich.skyward.openapi.model.ForbiddenResponseBody;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Component;

/**
 * Mapper class for handling AuthorizationDeniedException exceptions.
 * This mapper converts the exception to a standardized HTTP response with a 403 Forbidden status.
 */
@Component

public class AuthorizationDeniedExceptionExceptionMapper implements ExceptionMapper<AuthorizationDeniedException> {

    /**
     * Converts an AuthorizationDeniedException into an HTTP 403 Forbidden response.
     *
     * @param e the AuthorizationDeniedException to handle
     * @return a Response object with status 403 Forbidden and a body containing the error details
     */
    @Override
    public Response toResponse(AuthorizationDeniedException e) {
        ForbiddenResponseBody responseBody = new ForbiddenResponseBody("Forbidden", e.getMessage());
        return Response.status(Response.Status.FORBIDDEN).entity(responseBody).build();
    }
}