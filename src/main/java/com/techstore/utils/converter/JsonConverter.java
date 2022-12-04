package com.techstore.utils.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.techstore.model.dto.AuthenticationDto;

import java.io.IOException;
import java.io.OutputStream;

public class JsonConverter {
    public static <T> void toJson(OutputStream outputStream, T object) throws IOException {
        buildObjectMapper().writeValue(outputStream, object);
    }

    public static AuthenticationDto toJson(String jsonString) throws JsonProcessingException {
        return buildObjectMapper().readValue(jsonString, AuthenticationDto.class);
    }

    private static ObjectMapper buildObjectMapper() {
        return JsonMapper.builder().addModule(new JavaTimeModule()).build();
    }
}
