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
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.core.Response;
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
        List<Cep> lista = cepRepository.findAll();
        if (lista.isEmpty()) return List.of();

        return lista.stream()
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
            CepVo cepVo;
            try (Response response = client.buscaCep(cep)) {
                cepVo = response.readEntity(CepVo.class);
            }
            return Optional.of(cepVo);
        } catch (ProcessingException e) {
            LOG.error("A API ViaCep retornou vazio");
            throw new NegocioException("O CEP: " + cep + " não existe na base de dados da API Via Cep");
        } catch (Exception e) {
            LOG.error("Error na chamada da API");
            throw new InternalServerErrorException("Falha ao chamar API Via CEP");
        }
    }

}
