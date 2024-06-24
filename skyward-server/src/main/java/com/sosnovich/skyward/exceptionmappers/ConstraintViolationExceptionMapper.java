package com.sosnovich.skyward.exceptionmappers;

import com.sosnovich.skyward.openapi.model.InvalidInputErrorBody;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import org.springframework.stereotype.Component;

@Component
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    @Override
    public Response toResponse(ConstraintViolationException exception) {
        var errorMessage = exception.getConstraintViolations().stream()
                .map(this::mapViolationToValidationError)
                .toList();

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errorMessage)
                .build();
    }

    private InvalidInputErrorBody mapViolationToValidationError(ConstraintViolation<?> violation) {
        String field = violation.getPropertyPath().toString();
        String message = violation.getMessage();
        Object rejectedValue = violation.getInvalidValue();
        return new InvalidInputErrorBody(field,message,rejectedValue==null? "null":rejectedValue.toString());
    }
}