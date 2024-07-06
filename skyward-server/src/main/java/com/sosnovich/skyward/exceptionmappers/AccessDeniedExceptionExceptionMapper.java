package com.sosnovich.skyward.exceptionmappers;

import com.sosnovich.skyward.openapi.model.ForbiddenResponseBody;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

/**
 * Exception mapper for handling {@link AccessDeniedException}.
 *
 * <p>
 * This class is responsible for converting {@link AccessDeniedException}
 * into an HTTP 403 Forbidden response.
 * </p>
 *
 * <p>
 * It implements the {@link ExceptionMapper} interface and overrides the
 * {@code toResponse} method to build a custom response with the status code
 * 403 and a custom response body.
 * </p>
 *
 * <p>
 * The custom response body includes an error message indicating that access
 * to the requested resource was denied.
 * </p>
 *
 * <p>
 * This class is annotated with {@link Provider} to register it as a JAX-RS
 * provider and {@link Component} to allow Spring to manage its lifecycle.
 * </p>
 *
 * @see AccessDeniedException
 * @see ExceptionMapper
 */
@Provider
@Component
public class AccessDeniedExceptionExceptionMapper implements ExceptionMapper<AccessDeniedException> {

    /**
     * Converts an {@link AccessDeniedException} into an HTTP 403 Forbidden
     * response.
     *
     * <p>
     * This method builds a custom response with the status code 403 and a
     * {@link ForbiddenResponseBody} containing an error message.
     * </p>
     *
     * @param e the exception to be mapped
     * @return a {@link Response} object with status code 403 and a custom error
     *         message
     */
    @Override
    public Response toResponse(AccessDeniedException e) {
        return Response.status(Response.Status.FORBIDDEN)
                .entity(new ForbiddenResponseBody("Access Denied", e.getMessage()))
                .build();
    }
}
