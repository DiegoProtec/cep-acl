package com.desafio.exception.constraint;

import com.desafio.exception.ValidationError;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.List;
import java.util.stream.Collectors;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        List<ValidationError> errors = exception.getConstraintViolations().stream().map(this::mapToValidationError).collect(Collectors.toList());
        return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse("Erro de validação", errors)).build();
    }

    private ValidationError mapToValidationError(ConstraintViolation<?> violation) {
        String propertyPath = violation.getPropertyPath().toString();
        String field = propertyPath.contains(".") ? propertyPath.substring(propertyPath.lastIndexOf(".") + 1) : propertyPath;
        return new ValidationError(field, violation.getMessage());
    }
}

record ErrorResponse(String message, List<ValidationError> errors) {
}