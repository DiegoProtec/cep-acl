package com.desafio.config;

import com.desafio.config.aws.AmazonSecretsManagerConfig;
import org.eclipse.microprofile.config.spi.ConfigSource;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CustomConfigSource implements ConfigSource {

    private final Map<String, String> properties = new HashMap<>();

    public CustomConfigSource() {
        var secret = AmazonSecretsManagerConfig.getSecretDynamoDB();
        secret.ifPresent(dynamoDB -> {
            properties.put("AWS_ACCESS_KEY_ID", dynamoDB.accessKeyId());
            properties.put("AWS_SECRET_ACCESS_KEY", dynamoDB.secretAccessKey());
        });
    }

    @Override
    public Map<String, String> getProperties() {
        return properties;
    }

    @Override
    public Set<String> getPropertyNames() {
        return Set.of();
    }

    @Override
    public String getName() {
        return "CustomConfigSource";
    }

    @Override
    public String getValue(String propertyName) {
        return properties.get(propertyName);
    }

}
