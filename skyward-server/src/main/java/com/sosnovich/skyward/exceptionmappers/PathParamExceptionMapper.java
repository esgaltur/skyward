package com.sosnovich.skyward.exceptionmappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import org.glassfish.jersey.server.ParamException;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class PathParamExceptionMapper implements ExceptionMapper<ParamException.PathParamException> {
    @Override
    public Response toResponse(ParamException.PathParamException e) {
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
