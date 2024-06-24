package com.sosnovich.skyward.exceptionmappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import org.springframework.stereotype.Component;

@Component
public class ExceptionExceptionMapper implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception e) {
        return Response.status(Response.Status.EXPECTATION_FAILED).entity(e.getMessage()).build();
    }
}
