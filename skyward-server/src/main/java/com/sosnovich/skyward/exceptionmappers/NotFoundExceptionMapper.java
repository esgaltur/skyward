package com.sosnovich.skyward.exceptionmappers;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import org.springframework.stereotype.Component;

/**
 * Exception mapper for handling NotFoundException and returning an appropriate response.
 */
@Component
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    /**
     * Maps a NotFoundException to a HTTP response.
     *
     * @param e the NotFoundException to be mapped
     * @return a Response with status 404 (Not Found)
     */
    @Override
    public Response toResponse(NotFoundException e) {
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
