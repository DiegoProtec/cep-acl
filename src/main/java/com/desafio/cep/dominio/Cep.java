package com.desafio.cep.dominio;

import io.quarkus.runtime.annotations.RegisterForReflection;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;
import java.util.Optional;

@RegisterForReflection
public class Cep {

    private String cep;
    private String logradouro;
    private String bairro;
    private String localidade;
    private String estado;

    public Cep() {
    }

    public static Optional<Cep> from(Map<String, AttributeValue> item) {
        if (item == null || item.isEmpty())
            return Optional.empty();

        var cep = new Cep();
        cep.setCep(item.get(CepDocument.CEP_KEY).s());
        cep.setLogradouro(item.get(CepDocument.CEP_LOGRADOURO_COL).s());
        cep.setBairro(item.get(CepDocument.CEP_BAIRRO_COL).s());
        cep.setLocalidade(item.get(CepDocument.CEP_LOCALIDADE_COL).s());
        cep.setEstado(item.get(CepDocument.CEP_ESTADO_COL).s());
        return Optional.of(cep);
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getLocalidade() {
        return localidade;
    }

    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}

