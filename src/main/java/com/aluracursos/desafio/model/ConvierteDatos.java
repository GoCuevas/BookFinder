package com.aluracursos.desafio.model;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class ConvierteDatos {
    private final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public <T> T obtenerDatos(String json, Class<T> clazz) {
        try {
            if (json == null || json.isBlank()) return null;
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Error parseando JSON: " + e.getMessage(), e);
        }
    }
}
