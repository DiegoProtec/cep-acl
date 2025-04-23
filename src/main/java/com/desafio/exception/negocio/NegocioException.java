package com.desafio.exception.negocio;

public class NegocioException extends RuntimeException {

    public NegocioException(String message, Throwable cause) {
        super(message, cause);
    }
}
