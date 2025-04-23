package com.desafio.cep.application;

import com.desafio.cep.client.ViaCepClient;
import com.desafio.cep.dominio.CepMapper;
import com.desafio.cep.dominio.CepVo;
import com.desafio.cep.repository.CepRepository;
import com.desafio.exception.negocio.NegocioException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class CepService {

    private static final Logger LOG = LoggerFactory.getLogger(CepService.class);

    @RestClient
    ViaCepClient client;

    @Inject
    CepRepository cepRepository;

    public CepVo buscar(String cep) {
        return cepRepository.findByKey(cep)
                .map(CepMapper.INSTANCE::toVo)
                .orElseGet(() -> sincronizaTabelaCep(cep));
    }

    public List<CepVo> buscarTodos() {
        return cepRepository.findAll().stream()
                .map(CepMapper.INSTANCE::toVo)
                .collect(Collectors.toList());
    }

    private CepVo sincronizaTabelaCep(String cep) {
        CepVo vo = consultarApiViaCep(cep);
        cepRepository.save(CepMapper.INSTANCE.toDocument(vo));
        return vo;
    }

    private CepVo consultarApiViaCep(String cep) {
        try {
            return client.buscaCep(cep);
        } catch (Exception e) {
            var mensagemError = "VIACEP: Error ao buscar o CEP: " + cep;
            LOG.error(mensagemError, e);
            throw new NegocioException(mensagemError, e.getCause());
        }
    }

}
