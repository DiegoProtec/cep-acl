package com.desafio.exception;

import com.desafio.exception.negocio.NegocioException;
import com.desafio.util.Evento;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

public class ExceptionResponse {

    public int status;
    public String timestamp;
    public String titulo;
    public String descricao;
    public List<ErrorResponse> errors;

    public ExceptionResponse(List<ErrorResponse> errors) {
        this.status = 400;
        this.timestamp = Evento.getFormattedTimestamp();
        this.titulo = "Chamada inválida";
        this.descricao = "Error nos dados fornecidos para o recurso";
        this.errors = errors;
    }

    public ExceptionResponse(NegocioException e) {
        this.status = 422;
        this.timestamp = Evento.getFormattedTimestamp();
        this.titulo = "Negócio";
        this.descricao = e.getLocalizedMessage();
        this.errors = List.of();
    }

    public ExceptionResponse(NotFoundException e) {
        this.status = 404;
        this.timestamp = Evento.getFormattedTimestamp();
        this.titulo = "Recurso não encontrado";
        this.descricao = e.getLocalizedMessage();
        this.errors = List.of();
    }

    public ExceptionResponse(InternalServerErrorException e) {
        this.status = 500;
        this.timestamp = Evento.getFormattedTimestamp();
        this.titulo = "Error de aplicação";
        this.descricao = e.getLocalizedMessage();
        this.errors = List.of();
    }

}
