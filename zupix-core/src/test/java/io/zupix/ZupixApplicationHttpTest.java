package io.zupix;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ZupixApplicationHttpTest {

    @Test
    void servesAnnotatedGetRoute() throws Exception {
        try (ZupixApplication application = ZupixApplication.create(new ExampleApi(), 0)) {
            application.start();

            var request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:" + application.port() + "/hello"))
                    .GET()
                    .build();

            var response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(200, response.statusCode());
            assertEquals("Hello Zupix", response.body());
        }
    }

    @Test
    void returnsNotFoundForUnknownRoute() throws Exception {
        try (ZupixApplication application = ZupixApplication.create(new ExampleApi(), 0)) {
            application.start();

            var request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:" + application.port() + "/missing"))
                    .GET()
                    .build();

            var response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(404, response.statusCode());
        }
    }

    static final class ExampleApi {
        @Get("/hello")
        String hello() {
            return "Hello Zupix";
        }
    }
}
