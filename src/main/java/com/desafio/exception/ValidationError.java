package com.desafio.exception;

public record ValidationError(String field, String message) {
}
