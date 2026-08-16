package io.zupix;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ZupixHttpIntegrationTest {
    private final HttpClient client = HttpClient.newHttpClient();

    @Test
    void servesRouteThroughRealHttpPipeline() throws Exception {
        try (var app = ZupixApplication.create(new Api(), 0)) {
            app.start();
            var request = HttpRequest.newBuilder(URI.create("http://localhost:" + app.port() + "/hello"))
                    .GET().build();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertEquals("Hello Zupix!", response.body());
        }
    }

    @Test
    void returns404ForUnknownRoute() throws Exception {
        try (var app = ZupixApplication.create(new Api(), 0)) {
            app.start();
            var request = HttpRequest.newBuilder(URI.create("http://localhost:" + app.port() + "/missing"))
                    .GET().build();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(404, response.statusCode());
        }
    }

    static final class Api {
        @Get("/hello")
        String hello() { return "Hello Zupix!"; }
    }
}
