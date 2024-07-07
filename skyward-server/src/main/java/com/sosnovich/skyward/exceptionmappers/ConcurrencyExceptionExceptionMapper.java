package com.sosnovich.skyward.exceptionmappers;

import com.sosnovich.skyward.exc.ConcurrencyException;
import com.sosnovich.skyward.openapi.model.ConcurrencyExceptionBody;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import org.springframework.stereotype.Component;

/**
 * Mapper class to handle {@link ConcurrencyException} and convert it into a HTTP response.
 * This class implements {@link ExceptionMapper} to map {@link ConcurrencyException} to a
 * HTTP 409 Conflict response.
 */
@Component
public class ConcurrencyExceptionExceptionMapper implements ExceptionMapper<ConcurrencyException> {

    /**
     * Converts a {@link ConcurrencyException} to a HTTP 409 Conflict response.
     *
     * @param e the {@link ConcurrencyException} that was thrown
     * @return a {@link Response} with status 409 Conflict and a body containing details of the exception
     */
    @Override
    public Response toResponse(ConcurrencyException e) {
        return Response.status(Response.Status.CONFLICT.getStatusCode())
                .entity(new ConcurrencyExceptionBody("Resource modified", e.getMessage()))
                .build();
    }
}
