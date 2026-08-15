package io.zupix;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.Executors;

/** Minimal HTTP runtime for the Zupix foundation. */
public final class ZupixServer implements AutoCloseable {
    private final HttpServer server;
    private ZupixServer(HttpServer server) { this.server = server; }

    public static ZupixServer create(int port, Router router) throws IOException {
        Objects.requireNonNull(router, "router");
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", exchange -> handle(exchange, router));
        server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        return new ZupixServer(server);
    }
    public void start() { server.start(); }
    public int port() { return server.getAddress().getPort(); }
    @Override public void close() { server.stop(0); }

    private static void handle(HttpExchange exchange, Router router) throws IOException {
        Router.MatchedRoute matched = router.match(exchange.getRequestMethod(), exchange.getRequestURI().getPath());
        byte[] response;
        int status;
        String contentType = "text/plain; charset=utf-8";
        if (matched == null) {
            response = "Not Found".getBytes(StandardCharsets.UTF_8); status = 404;
        } else if (matched.route().handler() == null) {
            response = "Zupix route matched".getBytes(StandardCharsets.UTF_8); status = 200;
        } else {
            try {
                String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                Object result = matched.route().handler().invoke(matched.parameters(), exchange.getRequestURI().getRawQuery(), requestBody);
                if (result instanceof Response explicit) {
                    status = explicit.status();
                    applyHeaders(exchange, explicit.headers());
                    result = explicit.body();
                } else status = 200;
                if (result == null) response = new byte[0];
                else if (result instanceof String text) response = text.getBytes(StandardCharsets.UTF_8);
                else { response = Json.write(result).getBytes(StandardCharsets.UTF_8); contentType = "application/json; charset=utf-8"; }
            } catch (IllegalArgumentException exception) {
                response = exception.getMessage().getBytes(StandardCharsets.UTF_8); status = 400;
            } catch (RuntimeException exception) {
                response = "Internal Server Error".getBytes(StandardCharsets.UTF_8); status = 500;
            }
        }
        if (exchange.getResponseHeaders().getFirst("Content-Type") == null) exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(status, response.length);
        try (OutputStream output = exchange.getResponseBody()) { output.write(response); }
    }

    private static void applyHeaders(HttpExchange exchange, java.util.Map<String, String> headers) {
        headers.forEach((name, value) -> exchange.getResponseHeaders().set(name, value));
    }
}
