package com.desafio.exception.internalservererrorexception;

import com.desafio.exception.ExceptionResponse;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class InternalServerErrorExceptionMapper implements ExceptionMapper<InternalServerErrorException> {

    @Override
    public Response toResponse(InternalServerErrorException e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(e);
        return Response
                .status(500)
                .entity(exceptionResponse)
                .build();
    }

}