package io.zupix;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ZupixServerTest {

    @Test
    void servesRegisteredRoute() throws Exception {
        Router router = new Router().get("/hello");

        try (ZupixServer server = ZupixServer.create(0, router)) {
            server.start();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:" + server.port() + "/hello"))
                    .GET()
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(200, response.statusCode());
            assertEquals("Zupix route matched", response.body());
        }
    }

    @Test
    void returnsNotFoundForUnknownRoute() throws Exception {
        Router router = new Router().get("/hello");

        try (ZupixServer server = ZupixServer.create(0, router)) {
            server.start();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:" + server.port() + "/missing"))
                    .GET()
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(404, response.statusCode());
            assertEquals("Not Found", response.body());
        }
    }
}
