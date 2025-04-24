package com.desafio.exception.notfound;

import com.desafio.exception.ExceptionResponse;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    @Override
    public Response toResponse(NotFoundException e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(e);
        return Response
                .status(404)
                .entity(exceptionResponse)
                .build();
    }

}