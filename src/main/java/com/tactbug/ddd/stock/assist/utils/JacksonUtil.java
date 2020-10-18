package com.tactbug.ddd.stock.assist.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonUtil {

    private static ObjectMapper objectMapper;
    static {
        objectMapper = new ObjectMapper();
    }

    public static<T> String objectToString(T t) throws JsonProcessingException {
        return objectMapper.writeValueAsString(t);
    }

    public static<T> T stringToObject(String s, TypeReference<T> typeReference) throws JsonProcessingException {
        return objectMapper.readValue(s, typeReference);
    }
}
