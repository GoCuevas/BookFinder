package com.aluracursos.desafio.model;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.time.Duration;
import java.util.Set;

@Component
public class ConsumoAPI {

    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(20))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .version(HttpClient.Version.HTTP_2)
            .build();

    private static final Set<Integer> RETRY_STATUS = Set.of(500, 502, 503, 504);

    public String obtenerDatos(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(30)) // ↑ más tiempo
                .header("Accept", "application/json")
                .header("User-Agent", "Mozilla/5.0 (Java HttpClient) Gutendex Demo")
                .GET()
                .build();

        int maxAttempts = 3;
        long backoffMs = 800;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                HttpResponse<String> resp = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
                int sc = resp.statusCode();
                if (sc == 200 && resp.body() != null && !resp.body().isBlank()) {
                    return resp.body();
                }
                if (!RETRY_STATUS.contains(sc)) {
                    throw new RuntimeException("HTTP " + sc + " en " + url);
                }
                // status 5xx: reintentar
            } catch (HttpTimeoutException e) {
                // timeout: reintentar
            } catch (IOException e) {
                // problemas de red: reintentar
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Request interrumpida", e);
            }

            // Backoff antes del próximo intento
            if (attempt < maxAttempts) {
                try { Thread.sleep(backoffMs); } catch (InterruptedException ignored) { }
                backoffMs *= 2; // exponencial
            }
        }
        throw new RuntimeException("request timed out");
    }
}
