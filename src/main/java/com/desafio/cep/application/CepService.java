package com.desafio.cep.application;

import com.desafio.cep.client.ViaCepClient;
import com.desafio.cep.dominio.Cep;
import com.desafio.cep.dominio.CepMapper;
import com.desafio.cep.dominio.CepVo;
import com.desafio.cep.repository.CepRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class CepService {

    private static final Logger LOG = LoggerFactory.getLogger(CepService.class);

    @Inject
    Validator validator;

    @RestClient
    ViaCepClient client;

    @Inject
    CepRepository cepRepository;

    public CepVo buscar(
            @NotBlank(message = "O CEP é obrigatório")
            @Pattern(regexp = "\\d{8}", message = "CEP deve ter exatamente 8 dígitos")
            String cep) {
        CepVo cepVo;
        try {
            Cep cepDocument = cepRepository.findByKey(cep);
            cepVo = CepMapper.INSTANCE.toVo(cepDocument);
        } catch (NotFoundException e) {
            cepVo = viaCepBuscar(cep);
            salvar(cepVo);
        } catch (Exception e) {
            if (e.getCause() instanceof InternalServerErrorException) throw e;
            var mensagem = "Ocorreu uma falha interna ao buscar o CEP: " + cep;
            LOG.error(mensagem, e.getCause());
            throw new InternalServerErrorException(mensagem);
        }
        return cepVo;
    }

    public List<CepVo> buscarTodos() {
        List<Cep> lista = cepRepository.findAll();
        if (lista.isEmpty()) return List.of();

        return lista.stream()
                .map(CepMapper.INSTANCE::toVo)
                .collect(Collectors.toList());
    }

    private CepVo viaCepBuscar(String cep) {
        try {
            Response response = client.buscaCep(cep);
            return response.readEntity(CepVo.class);
        } catch (ProcessingException e) {
            if (e.getCause() instanceof UnknownHostException) {
                var mensagem = "Falha na comunicação com a API ViaCep";
                LOG.error(mensagem, e.getCause());
                throw new InternalServerErrorException(mensagem);
            }
            var mensagem = "O serviço ViaCep não encontrou o CEP: " + cep;
            LOG.error(mensagem, e.getCause());
            throw new NotFoundException(mensagem);
        } catch (Exception e) {
            var mensagem = "Falha na comunicação com a API ViaCep";
            LOG.error(mensagem, e.getCause());
            throw new InternalServerErrorException(mensagem);
        }
    }

    private void salvar(CepVo cep) {
        Set<ConstraintViolation<CepVo>> violations = validator.validate(cep);
        if (!violations.isEmpty()) throw new ConstraintViolationException(violations);
        cepRepository.save(CepMapper.INSTANCE.toDocument(cep));
    }

}
