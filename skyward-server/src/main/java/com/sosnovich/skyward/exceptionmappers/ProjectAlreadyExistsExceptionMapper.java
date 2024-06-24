package com.sosnovich.skyward.exceptionmappers;

import com.sosnovich.skyward.exc.ProjectAlreadyExistsException;
import com.sosnovich.skyward.openapi.model.ProjectAlreadyExistBody;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import org.springframework.stereotype.Component;

@Component
public class ProjectAlreadyExistsExceptionMapper implements ExceptionMapper<ProjectAlreadyExistsException> {
    @Override
    public Response toResponse(ProjectAlreadyExistsException e) {
        return Response.status(Response.Status.CONFLICT).entity(new ProjectAlreadyExistBody(e.getMessage())).build();
    }
}
