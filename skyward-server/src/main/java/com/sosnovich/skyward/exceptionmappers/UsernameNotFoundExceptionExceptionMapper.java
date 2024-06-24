package com.sosnovich.skyward.exceptionmappers;

import com.sosnovich.skyward.openapi.model.UnauthorizedBody;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * Exception mapper for handling UsernameNotFoundException and returning an appropriate response.
 */
@Component
public class UsernameNotFoundExceptionExceptionMapper implements ExceptionMapper<UsernameNotFoundException> {

    /**
     * Maps a UsernameNotFoundException to a HTTP response.
     *
     * @param e the UsernameNotFoundException to be mapped
     * @return a Response with status 404 (Not Found) and an error message
     */
    @Override
    public Response toResponse(UsernameNotFoundException e) {
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(new UnauthorizedBody(e.getMessage()))
                .build();
    }
}
