package com.desafio.config.aws.secrets;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DynamoDB(
        String regiao,
        @JsonProperty("access-key")
        String accessKey,
        @JsonProperty("secret-access-key")
        String secretAccessKey
) {
}
