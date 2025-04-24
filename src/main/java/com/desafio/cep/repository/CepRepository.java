package com.desafio.cep.repository;

import com.desafio.cep.dominio.Cep;
import com.desafio.cep.dominio.CepDocument;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;

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
        } catch (ResourceNotFoundException e) {
            var mensagem = "A tabela CEP não existe";
            LOG.error(mensagem, e.getCause());
            throw new InternalServerErrorException(mensagem);
        } catch (Exception e) {
            var mensagem = "Falha na comunicação com a base de dados";
            LOG.error(mensagem, e.getCause());
            throw new InternalServerErrorException(mensagem);
        }
    }

    public Cep findByKey(String cep) {
        try {
            GetItemResponse response = dynamoDbClient.getItem(getRequest(cep));
            return Cep.from(response.item())
                    .orElseThrow(NotFoundException::new);
        } catch (NotFoundException | ResourceNotFoundException e) {
            var mensagem = "Não foi encontrado na base de dados o CEP: " + cep;
            LOG.error(mensagem, e.getCause());
            throw new NotFoundException(mensagem);
        } catch (Exception e) {
            var mensagem = "Falha na comunicação com a base de dados";
            LOG.error(mensagem, e.getCause());
            throw new InternalServerErrorException(mensagem);
        }
    }

    public List<Cep> findAll() {
        try {
            return dynamoDbClient.scanPaginator(scanRequest())
                    .items().stream()
                    .map(Cep::from)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());

        } catch (ResourceNotFoundException e) {
            LOG.error("Não foi encontrado registros na tabela CEP", e.getCause());
            return List.of();
        } catch (Exception e) {
            var mensagem = "Falha na comunicação com a base de dados";
            LOG.error(mensagem, e.getCause());
            throw new InternalServerErrorException(mensagem);
        }
    }

}
