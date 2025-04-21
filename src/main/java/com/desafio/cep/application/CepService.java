package com.desafio.cep.application;

import com.desafio.cep.client.ViaCepClient;
import com.desafio.cep.dominio.CepVo;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class CepService {

    @RestClient
    ViaCepClient client;

    public CepVo buscar(String cep) {
        return client.buscaCep(cep);
    }

}
