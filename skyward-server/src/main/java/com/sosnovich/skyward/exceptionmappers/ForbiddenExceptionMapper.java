package com.sosnovich.skyward.exceptionmappers;

import com.sosnovich.skyward.openapi.model.ForbiddenResponseBody;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

/**
 * Exception mapper for handling {@link ForbiddenException}.
 * This mapper converts a ForbiddenException into a HTTP 403 Forbidden response.
 */
public class ForbiddenExceptionMapper implements ExceptionMapper<ForbiddenException> {

    /**
     * Converts a {@link ForbiddenException} into a HTTP 403 Forbidden response.
     *
     * @param e the ForbiddenException to convert
     * @return a Response object with status 403 and a ForbiddenResponseBody entity
     */
    @Override
    public Response toResponse(ForbiddenException e) {
        return Response.status(Response.Status.FORBIDDEN)
                .entity(new ForbiddenResponseBody("Forbidden", e.getMessage()))
                .build();
    }
}
