package com.techstore.utils.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.io.OutputStream;

public class JsonConverter {
    public static <T> void toJson(OutputStream outputStream, T object) throws IOException {
        buildObjectMapper().writeValue(outputStream, object);
    }

    public static <T> T toJson(String jsonString, Class<T> objectClass) throws JsonProcessingException {
        return buildObjectMapper().readValue(jsonString, objectClass);
    }

    private static ObjectMapper buildObjectMapper() {
        return JsonMapper.builder().addModule(new JavaTimeModule()).build();
    }
}
