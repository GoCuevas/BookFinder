package com.aluracursos.desafio.client;

import com.aluracursos.desafio.model.ConvierteDatos;
import com.aluracursos.desafio.model.ConsumoAPI;
import com.aluracursos.desafio.model.Datos;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class GutendexClient {
    private static final String BASE = "https://gutendex.com/books/";

    private final ConsumoAPI consumoAPI;
    private final ConvierteDatos convierteDatos;

    public GutendexClient(ConsumoAPI consumoAPI, ConvierteDatos convierteDatos) {
        this.consumoAPI = consumoAPI;
        this.convierteDatos = convierteDatos;
    }

    public Datos search(String query) {
        String q = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String url = BASE + "?search=" + q + "&sort=popular";
        String json = consumoAPI.obtenerDatos(url);
        return convierteDatos.obtenerDatos(json, Datos.class);
    }
}
