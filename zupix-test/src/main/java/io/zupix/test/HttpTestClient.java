package io.zupix.test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

/** Small Java HTTP client helper for black-box tests against a running Zupix server. */
public final class HttpTestClient {
    private final HttpClient client;
    private final String baseUrl;

    public HttpTestClient(String baseUrl) {
        this.client = HttpClient.newHttpClient();
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    }

    public TestHttpResponse get(String path) throws IOException, InterruptedException {
        return send("GET", path, "", Map.of());
    }

    public TestHttpResponse post(String path, String body) throws IOException, InterruptedException {
        return send("POST", path, body, Map.of("Content-Type", "application/json"));
    }

    public TestHttpResponse send(String method, String path, String body, Map<String, String> headers)
            throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(baseUrl + path))
                .method(method, HttpRequest.BodyPublishers.ofString(body == null ? "" : body));
        headers.forEach(builder::header);
        HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        return new TestHttpResponse(response.statusCode(), response.body(), response.headers().map());
    }

    public record TestHttpResponse(int status, String body, Map<String, java.util.List<String>> headers) {
        public String header(String name) {
            return headers.entrySet().stream().filter(e -> e.getKey().equalsIgnoreCase(name))
                    .flatMap(e -> e.getValue().stream()).findFirst().orElse(null);
        }
    }
}
