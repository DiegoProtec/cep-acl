package com.desafio.exception.negocio;

import com.desafio.exception.ExceptionResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class NegocioExceptionMapper implements ExceptionMapper<NegocioException> {

    @Override
    public Response toResponse(NegocioException e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(e);
        return Response
                .status(422)
                .entity(exceptionResponse)
                .build();
    }

}