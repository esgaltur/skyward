package com.sosnovich.skyward.exceptionmappers;

import com.sosnovich.skyward.openapi.model.UnauthorizedBody;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

/**
 * Exception mapper for handling {@link BadCredentialsException}.
 * This mapper converts a BadCredentialsException into a HTTP 401 Unauthorized response.
 */
@Component
public class BadCredentialsExceptionMapper implements ExceptionMapper<BadCredentialsException> {

    /**
     * Converts a {@link BadCredentialsException} into a HTTP 401 Unauthorized response.
     *
     * @param e the BadCredentialsException to convert
     * @return a Response object with status 401 and a ForbiddenResponseBody entity
     */
    @Override
    public Response toResponse(BadCredentialsException e) {
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(new UnauthorizedBody(e.getMessage()))
                .build();
    }
}
