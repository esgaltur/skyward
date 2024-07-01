package com.sosnovich.skyward.exceptionmappers;

import com.sosnovich.skyward.exc.ProjectAlreadyExistsException;
import com.sosnovich.skyward.openapi.model.ProjectAlreadyExistBody;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import org.springframework.stereotype.Component;

/**
 * Exception mapper for handling {@link ProjectAlreadyExistsException}.
 * This mapper converts a ProjectAlreadyExistsException into a HTTP 409 Conflict response.
 */
@Component
public class ProjectAlreadyExistsExceptionMapper implements ExceptionMapper<ProjectAlreadyExistsException> {

    /**
     * Converts a {@link ProjectAlreadyExistsException} into a HTTP 409 Conflict response.
     *
     * @param e the ProjectAlreadyExistsException to convert
     * @return a Response object with status 409 and a ProjectAlreadyExistBody entity
     */
    @Override
    public Response toResponse(ProjectAlreadyExistsException e) {
        return Response.status(Response.Status.CONFLICT)
                .entity(new ProjectAlreadyExistBody(e.getMessage()))
                .build();
    }
}
