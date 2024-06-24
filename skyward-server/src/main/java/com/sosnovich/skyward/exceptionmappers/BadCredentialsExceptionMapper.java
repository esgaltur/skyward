package com.sosnovich.skyward.exceptionmappers;

import com.sosnovich.skyward.openapi.model.ForbiddenResponseBody;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

@Component
public class BadCredentialsExceptionMapper implements ExceptionMapper<BadCredentialsException> {
    @Override
    public Response toResponse(BadCredentialsException e) {
        return Response.status(Response.Status.UNAUTHORIZED).entity(new ForbiddenResponseBody("Unauthorized",e.getMessage())).build();
    }
}
