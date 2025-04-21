package com.desafio.cep.dominio;

public record CepVo(
        String cep,
        String logradouro,
        String bairro,
        String localidade,
        String estado
) {
    public CepVo(String cep,
                 String logradouro,
                 String bairro,
                 String localidade,
                 String estado) {
        this.cep = cep.replaceAll("[^0-9]", "");
        this.logradouro = logradouro;
        this.bairro = bairro;
        this.localidade = localidade;
        this.estado = estado;
    }
}
