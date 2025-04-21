package com.desafio.cep.dominio;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;

import java.util.HashMap;
import java.util.Map;

public abstract class CepDocument {

    public final static String CEP_TABLE_NAME = "cep";
    public static final String CEP_KEY = "cep";
    public static final String CEP_LOGRADOURO_COL = "logradouro";
    public static final String CEP_BAIRRO_COL = "bairro";
    public static final String CEP_LOCALIDADE_COL = "localidade";
    public static final String CEP_ESTADO_COL = "estado";

    protected ScanRequest scanRequest() {
        return ScanRequest.builder().tableName(CEP_TABLE_NAME)
                .attributesToGet(CEP_KEY, CEP_LOGRADOURO_COL, CEP_BAIRRO_COL, CEP_LOCALIDADE_COL, CEP_ESTADO_COL).build();
    }

    protected PutItemRequest putRequest(Cep cep) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put(CEP_KEY, AttributeValue.builder().s(cep.getCep()).build());
        item.put(CEP_LOGRADOURO_COL, AttributeValue.builder().s(cep.getLogradouro()).build());
        item.put(CEP_BAIRRO_COL, AttributeValue.builder().s(cep.getBairro()).build());
        item.put(CEP_LOCALIDADE_COL, AttributeValue.builder().s(cep.getLocalidade()).build());
        item.put(CEP_ESTADO_COL, AttributeValue.builder().s(cep.getEstado()).build());

        return PutItemRequest.builder()
                .tableName(CEP_TABLE_NAME)
                .item(item)
                .build();
    }

    protected GetItemRequest getRequest(String name) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put(CEP_KEY, AttributeValue.builder().s(name).build());

        return GetItemRequest.builder()
                .tableName(CEP_TABLE_NAME)
                .key(key)
                .attributesToGet(CEP_KEY, CEP_LOGRADOURO_COL, CEP_BAIRRO_COL, CEP_LOCALIDADE_COL, CEP_ESTADO_COL)
                .build();
    }

}
