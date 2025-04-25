package com.desafio.config.aws;

import com.desafio.config.aws.secrets.DynamoDB;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.InternalServerErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import java.util.Optional;

public class AmazonSecretsManagerConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(AmazonSecretsManagerConfig.class);

    public static final String DYNAMODB_SECRET = "aws-secret";
    public static final String REGIAO_NAME = "sa-east-1";
    public static final Region REGIAO = Region.of(REGIAO_NAME);

    public static Optional<DynamoDB> getSecretDynamoDB() {
        GetSecretValueResponse getSecretValueResponse;
        SecretsManagerClient client = SecretsManagerClient.builder()
                .region(REGIAO)
                .build();
        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(DYNAMODB_SECRET)
                .build();
        try {
            getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
        } catch (SdkClientException e) {
            throw new InternalServerErrorException("Error ao recuperar secret: {}", e.getCause());
        }
        return jsonToDynamoDB(getSecretValueResponse.secretString());
    }

    public static Optional<DynamoDB> jsonToDynamoDB(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return Optional.of(objectMapper.readValue(jsonString, DynamoDB.class));
        } catch (Exception e) {
            LOGGER.error("Error converting JSON to DynamoDB: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }

}
