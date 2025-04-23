package com.desafio.exception.error;

public class InternalServerError extends RuntimeException {

    public InternalServerError(String message, Throwable cause) {
        super(message, cause);
    }

}
