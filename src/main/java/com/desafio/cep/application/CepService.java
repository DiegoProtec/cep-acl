package com.desafio.cep.application;

import com.desafio.cep.client.ViaCepClient;
import com.desafio.cep.dominio.Cep;
import com.desafio.cep.dominio.CepMapper;
import com.desafio.cep.dominio.CepVo;
import com.desafio.cep.repository.CepRepository;
import com.desafio.exception.negocio.NegocioException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class CepService {

    private static final Logger LOG = LoggerFactory.getLogger(CepService.class);

    @RestClient
    ViaCepClient client;

    @Inject
    CepRepository cepRepository;

    public List<CepVo> buscarTodos() {
        return cepRepository.findAll().stream()
                .map(CepMapper.INSTANCE::toVo)
                .collect(Collectors.toList());
    }

    public CepVo buscar(
            @NotBlank(message = "O CEP é obrigatório")
            @Pattern(regexp = "\\d{8}", message = "CEP deve ter 8 dígitos")
            String cep) throws NegocioException {
        Optional<Cep> optCep = cepRepository.findByKey(cep);
        if (optCep.isPresent())
            return CepMapper.INSTANCE.toVo(optCep.get());

        Optional<CepVo> optCepVo = buscaCepApi(cep);
        if (optCepVo.isPresent()) {
            cepRepository.save(CepMapper.INSTANCE.toDocument(optCepVo.get()));
            return optCepVo.get();
        }

        throw new NegocioException("Error ao consultar o CEP: " + cep);
    }

    private Optional<CepVo> buscaCepApi(String cep) {
        try {
            return Optional.of(client.buscaCep(cep));
        } catch (Exception e) {
            LOG.error("Error ao buscar o CEP: {}", cep, e);
            return Optional.empty();
        }
    }

}
