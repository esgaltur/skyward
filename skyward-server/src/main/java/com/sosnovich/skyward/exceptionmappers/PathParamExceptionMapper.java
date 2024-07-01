package com.sosnovich.skyward.exceptionmappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import org.glassfish.jersey.server.ParamException;
import org.springframework.stereotype.Component;

/**
 * Exception mapper for handling {@link ParamException.PathParamException}.
 * This mapper converts a PathParamException into a HTTP 404 Not Found response.
 */
@Component
public class PathParamExceptionMapper implements ExceptionMapper<ParamException.PathParamException> {

    /**
     * Converts a {@link ParamException.PathParamException} into a HTTP 404 Not Found response.
     *
     * @param e the PathParamException to convert
     * @return a Response object with status 404
     */
    @Override
    public Response toResponse(ParamException.PathParamException e) {
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
