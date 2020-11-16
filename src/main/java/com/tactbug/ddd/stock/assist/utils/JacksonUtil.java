package com.tactbug.ddd.stock.assist.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tactbug.ddd.stock.assist.exception.TactStockException;
import com.tactbug.ddd.stock.assist.vo.BaseResultEnum;

import java.util.Map;

public class JacksonUtil {

    private static final ObjectMapper objectMapper;
    static {
        objectMapper = new ObjectMapper();
    }

    public static<T> String objectToString(T t) {
        try {
            return objectMapper.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            throw new TactStockException(BaseResultEnum.JSON_ERROR.message());
        }
    }

    public static<T> T stringToObject(String s, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(s, typeReference);
        } catch (JsonProcessingException e) {
            throw new TactStockException(BaseResultEnum.JSON_ERROR.message());
        }
    }

}
