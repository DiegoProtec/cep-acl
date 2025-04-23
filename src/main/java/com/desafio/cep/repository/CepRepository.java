package com.desafio.cep.repository;

import com.desafio.cep.dominio.Cep;
import com.desafio.cep.dominio.CepDocument;
import com.desafio.cep.exception.negocio.NegocioException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class CepRepository extends CepDocument {

    private static final Logger LOG = LoggerFactory.getLogger(CepRepository.class);

    @Inject
    DynamoDbClient dynamoDbClient;

    public void save(Cep cep) {
        try {
            dynamoDbClient.putItem(putRequest(cep));
        } catch (DynamoDbException e) {
            var mensagemError = "DYNAMODB: Error ao persistir o CEP: " + cep;
            LOG.error(mensagemError, e);
            throw new NegocioException(mensagemError, e.getCause());
        }
    }

    public Optional<Cep> findByKey(String cep) {
        try {
            var response = dynamoDbClient.getItem(getRequest(cep));
            if (response.hasItem()) {
                return Optional.of(Cep.from(response.item()));
            } else {
                return Optional.empty();
            }
        } catch (DynamoDbException e) {
            var mensagemError = "DYNAMODB: Error ao buscar o CEP: " + cep;
            LOG.error(mensagemError, e.getCause());
            return Optional.empty();
        }
    }

    public List<Cep> findAll() {
        return dynamoDbClient.scanPaginator(scanRequest()).items().stream()
                .map(Cep::from)
                .collect(Collectors.toList());
    }
}
