package com.desafio.config.aws.secrets;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DynamoDB(
        @JsonProperty("AccessKeyId")
        String accessKeyId,
        @JsonProperty("SecretAccessKey")
        String secretAccessKey
) {
}
