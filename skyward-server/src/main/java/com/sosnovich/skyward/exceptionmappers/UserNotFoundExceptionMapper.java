package com.sosnovich.skyward.exceptionmappers;

import com.sosnovich.skyward.exc.UserNotFoundException;
import com.sosnovich.skyward.openapi.model.GetUserById404Response;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import org.springframework.stereotype.Component;

/**
 * Exception mapper for handling {@link UserNotFoundException}.
 * This mapper converts a UserNotFoundException into a HTTP 404 Not Found response.
 */
@Component
public class UserNotFoundExceptionMapper implements ExceptionMapper<UserNotFoundException> {

    /**
     * Converts a {@link UserNotFoundException} into a HTTP 404 Not Found response.
     *
     * @param e the UserNotFoundException to convert
     * @return a Response object with status 404 and a GetUserById404Response entity
     */
    @Override
    public Response toResponse(UserNotFoundException e) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new GetUserById404Response(e.getMessage()))
                .build();
    }
}
