package com.sosnovich.skyward.exceptionmappers;

import com.sosnovich.skyward.exc.UserNotFoundException;
import com.sosnovich.skyward.openapi.model.GetUserById404Response;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import org.springframework.stereotype.Component;

@Component
public class UserNotFoundExceptionMapper implements ExceptionMapper<UserNotFoundException> {
    @Override
    public Response toResponse(UserNotFoundException e) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new GetUserById404Response(e.getMessage()))
                .build();
    }
}
