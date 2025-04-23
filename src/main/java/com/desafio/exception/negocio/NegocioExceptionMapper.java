package com.desafio.exception.negocio;

import com.desafio.exception.ErrorResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class NegocioExceptionMapper implements ExceptionMapper<NegocioException> {

    @Override
    public Response toResponse(NegocioException e) {
        return Response
                .status(Response.Status.EXPECTATION_FAILED)
                .entity(new ErrorResponse("Erro de negócio", e.getMessage()))
                .build();
    }

}