package com.desafio.cep.application;

import com.desafio.cep.client.ViaCepClient;
import com.desafio.cep.dominio.CepMapper;
import com.desafio.cep.dominio.CepVo;
import com.desafio.cep.exception.negocio.NegocioException;
import com.desafio.cep.repository.CepRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class CepService {

    @RestClient
    ViaCepClient client;

    @Inject
    CepRepository cepRepository;

    public CepVo buscar(String cep) {
        return cepRepository.findByKey(cep)
                .map(CepMapper.INSTANCE::toVo)
                .orElseGet(() -> sincronizaTabelaCep(cep));
    }

    private CepVo sincronizaTabelaCep(String cep) {
        try {
            CepVo vo = client.buscaCep(cep);
            cepRepository.save(CepMapper.INSTANCE.toDocument(vo));
            return vo;
        } catch (Exception e) {
            throw new NegocioException("Falha ao buscar o CEP: " + cep, e);
        }
    }

    public List<CepVo> buscar() {
        return cepRepository.findAll().stream()
                .map(CepMapper.INSTANCE::toVo)
                .collect(Collectors.toList());
    }

}
