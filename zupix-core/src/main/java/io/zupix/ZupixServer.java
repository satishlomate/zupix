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
        Router.MatchedRoute matched = router.match(
                exchange.getRequestMethod(), exchange.getRequestURI().getPath());

        byte[] body;
        int status;
        if (matched == null) {
            body = "Not Found".getBytes(StandardCharsets.UTF_8);
            status = 404;
        } else if (matched.route().handler() == null) {
            body = "Zupix route matched".getBytes(StandardCharsets.UTF_8);
            status = 200;
        } else {
            try {
                Object result = matched.route().handler().invoke(matched.parameters());
                body = String.valueOf(result).getBytes(StandardCharsets.UTF_8);
                status = 200;
            } catch (RuntimeException exception) {
                body = "Internal Server Error".getBytes(StandardCharsets.UTF_8);
                status = 500;
            }
        }

        exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=utf-8");
        exchange.sendResponseHeaders(status, body.length);
        try (OutputStream output = exchange.getResponseBody()) {
            output.write(body);
        }
    }
}
