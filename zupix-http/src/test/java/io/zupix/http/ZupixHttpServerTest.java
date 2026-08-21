package io.zupix.http;

import io.zupix.ExceptionRegistry;
import io.zupix.MiddlewareRegistry;
import io.zupix.Router;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ZupixHttpServerTest {
    private final HttpClient client = HttpClient.newHttpClient();

    @Test
    void servesRegisteredRoute() throws Exception {
        Router router = new Router().get("/hello");
        try (ZupixHttpServer server = ZupixHttpServer.create(0, router,
                new MiddlewareRegistry(), new ExceptionRegistry())) {
            server.start();
            HttpResponse<String> response = client.send(request(server, "/hello"),
                    HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertEquals("Zupix route matched", response.body());
        }
    }

    @Test
    void returnsNotFoundForUnknownRoute() throws Exception {
        Router router = new Router().get("/hello");
        try (ZupixHttpServer server = ZupixHttpServer.create(0, router,
                new MiddlewareRegistry(), new ExceptionRegistry())) {
            server.start();
            HttpResponse<String> response = client.send(request(server, "/missing"),
                    HttpResponse.BodyHandlers.ofString());
            assertEquals(404, response.statusCode());
            assertEquals("Not Found", response.body());
        }
    }

    private static HttpRequest request(ZupixHttpServer server, String path) {
        return HttpRequest.newBuilder(URI.create("http://localhost:" + server.port() + path))
                .GET().build();
    }
}
