package com.desafio.cep.exception.negocio;

public class NegocioException extends RuntimeException {

    public NegocioException(String message, Throwable cause) {
        super(message, cause);
    }
}
