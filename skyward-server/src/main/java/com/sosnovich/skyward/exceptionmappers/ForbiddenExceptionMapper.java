package com.sosnovich.skyward.exceptionmappers;

import com.sosnovich.skyward.openapi.model.ForbiddenResponseBody;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class ForbiddenExceptionMapper implements ExceptionMapper<ForbiddenException> {
    @Override
    public Response toResponse(ForbiddenException e) {
        return Response.status(Response.Status.FORBIDDEN).entity(new ForbiddenResponseBody("Forbidden",e.getMessage())).build();
    }
}
