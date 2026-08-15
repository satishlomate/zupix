package io.zupix;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.Executors;

/**
 * Minimal HTTP runtime for the Zupix foundation.
 *
 * <p>This first runtime intentionally uses the JDK HTTP server so the core
 * can run without an external server dependency. The transport abstraction
 * can be replaced or extended later without changing the public routing API.</p>
 */
public final class ZupixServer implements AutoCloseable {
    private final HttpServer server;

    private ZupixServer(HttpServer server) {
        this.server = server;
    }

    public static ZupixServer create(int port, Router router) throws IOException {
        Objects.requireNonNull(router, "router");

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", exchange -> handle(exchange, router));
        server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        return new ZupixServer(server);
    }

    public void start() {
        server.start();
    }

    public int port() {
        return server.getAddress().getPort();
    }

    @Override
    public void close() {
        server.stop(0);
    }

    private static void handle(HttpExchange exchange, Router router) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        boolean found = router.routes().stream()
                .anyMatch(route -> route.method().equals(method) && route.path().equals(path));

        byte[] body;
        int status;
        if (found) {
            body = "Zupix route matched".getBytes(StandardCharsets.UTF_8);
            status = 200;
        } else {
            body = "Not Found".getBytes(StandardCharsets.UTF_8);
            status = 404;
        }

        exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=utf-8");
        exchange.sendResponseHeaders(status, body.length);
        try (OutputStream output = exchange.getResponseBody()) {
            output.write(body);
        }
    }
}
