package com.sosnovich.skyward.exceptionmappers;

import com.sosnovich.skyward.openapi.model.InvalidInputErrorBody;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Exception mapper for handling {@link ConstraintViolationException}.
 * This mapper converts a ConstraintViolationException into a HTTP 400 Bad Request response.
 */
@Component
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    /**
     * Converts a {@link ConstraintViolationException} into a HTTP 400 Bad Request response.
     *
     * @param exception the ConstraintViolationException to convert
     * @return a Response object with status 400 and a list of InvalidInputErrorBody entities
     */
    @Override
    public Response toResponse(ConstraintViolationException exception) {
        List<InvalidInputErrorBody> errorMessage = exception.getConstraintViolations().stream()
                .map(this::mapViolationToValidationError)
                .toList();

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errorMessage)
                .build();
    }

    /**
     * Maps a {@link ConstraintViolation} to an {@link InvalidInputErrorBody}.
     *
     * @param violation the constraint violation to map
     * @return the corresponding InvalidInputErrorBody
     */
    private InvalidInputErrorBody mapViolationToValidationError(ConstraintViolation<?> violation) {
        String field = violation.getPropertyPath().toString();
        String message = violation.getMessage();
        Object rejectedValue = violation.getInvalidValue();
        return new InvalidInputErrorBody(field, message, rejectedValue == null ? "null" : rejectedValue.toString());
    }
}
