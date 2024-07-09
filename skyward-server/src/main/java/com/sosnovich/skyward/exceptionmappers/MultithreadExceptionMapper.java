package com.sosnovich.skyward.exceptionmappers;

import com.sosnovich.skyward.exc.MultithreadingException;
import com.sosnovich.skyward.openapi.model.InternalServerErrorBody;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Exception mapper for handling MultithreadingException and returning an appropriate response.
 */
@Component
@Slf4j
public class MultithreadExceptionMapper implements ExceptionMapper<MultithreadingException> {

    /**
     * Maps a MultithreadingException to a HTTP response.
     *
     * @param e the MultithreadingException to be mapped
     * @return a Response with status 500 (Internal Server Error) and an error message
     */
    @Override
    public Response toResponse(MultithreadingException e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new InternalServerErrorBody(e.getMessage()))
                .build();
    }
}
