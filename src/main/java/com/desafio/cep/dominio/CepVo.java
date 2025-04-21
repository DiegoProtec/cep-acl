package com.desafio.cep.dominio;

public record CepVo(
        String cep,
        String logradouro,
        String bairro,
        String localidade,
        String estado
) {
}
