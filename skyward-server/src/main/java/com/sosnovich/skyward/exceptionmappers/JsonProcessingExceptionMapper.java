package com.sosnovich.skyward.exceptionmappers;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sosnovich.skyward.openapi.model.InvalidInputError;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import org.springframework.stereotype.Component;

@Component
public class JsonProcessingExceptionMapper implements ExceptionMapper<JsonProcessingException> {

    @Override
    public Response toResponse(JsonProcessingException exception) {
        JsonLocation location = exception.getLocation();
        String message = exception.getOriginalMessage();

        Long line = (location != null) ? (long) location.getLineNr() : null;
        Long column = (location != null) ? (long) location.getColumnNr() : null;

        InvalidInputError errorBody = new InvalidInputError(message, line, column);
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errorBody)
                .build();
    }
}
