package com.sosnovich.skyward.exceptionmappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import org.springframework.stereotype.Component;

/**
 * Exception mapper for handling generic {@link Exception}.
 * This mapper converts a generic Exception into a HTTP 417 Expectation Failed response.
 */
@Component
public class ExceptionExceptionMapper implements ExceptionMapper<Exception> {

    /**
     * Converts a generic {@link Exception} into a HTTP 417 Expectation Failed response.
     *
     * @param e the Exception to convert
     * @return a Response object with status 417 and the exception message
     */
    @Override
    public Response toResponse(Exception e) {
        return Response.status(Response.Status.EXPECTATION_FAILED).entity(e.getMessage()).build();
    }
}
