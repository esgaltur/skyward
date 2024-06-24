package com.sosnovich.skyward.exceptionmappers;

import com.sosnovich.skyward.exc.EmailAlreadyInUseException;
import com.sosnovich.skyward.openapi.model.EmailInUseErrorBody;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import org.springframework.stereotype.Component;

/**
 * Exception mapper for handling EmailAlreadyInUseException and returning an appropriate response.
 */
@Component
public class EmailAlreadyInUseExceptionMapper implements ExceptionMapper<EmailAlreadyInUseException> {

    /**
     * Maps an EmailAlreadyInUseException to an HTTP response.
     *
     * @param e the EmailAlreadyInUseException to be mapped
     * @return a Response with status 409 (Conflict) and an error message
     */
    @Override
    public Response toResponse(EmailAlreadyInUseException e) {
         return Response.status(Response.Status.CONFLICT)
                .entity(new EmailInUseErrorBody(e.getMessage()))
                .build();
    }
}
