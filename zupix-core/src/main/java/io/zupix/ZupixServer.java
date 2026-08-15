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
    private final Router router;
    private ZupixServer(HttpServer server, Router router) { this.server = server; this.router = router; }
    public static ZupixServer create(int port, Router router) throws IOException {
        Objects.requireNonNull(router, "router");
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        ZupixServer runtime = new ZupixServer(server, router);
        server.createContext("/", runtime::handle);
        server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        return runtime;
    }
    public void start() { server.start(); }
    public int port() { return server.getAddress().getPort(); }
    @Override public void close() { server.stop(0); }

    private void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if ("/openapi.json".equals(path) && "GET".equals(exchange.getRequestMethod())) {
            write(exchange, 200, Json.write(new OpenApiDocumentGenerator().generate(router)), "application/json; charset=utf-8");
            return;
        }
        if ("/docs".equals(path) && "GET".equals(exchange.getRequestMethod())) {
            String html = "<!doctype html><html><head><meta charset='utf-8'><title>Zupix API Docs</title>"
                    + "<style>body{font-family:system-ui;margin:40px;max-width:900px}pre{background:#f5f5f5;padding:16px;overflow:auto}"
                    + "button{padding:8px 12px;cursor:pointer}</style></head><body>"
                    + "<h1>Zupix API</h1><p>Interactive API documentation.</p>"
                    + "<button onclick='loadSpec()'>Load OpenAPI</button><pre id='spec'>Loading...</pre>"
                    + "<script>async function loadSpec(){const r=await fetch('/openapi.json');"
                    + "document.getElementById('spec').textContent=JSON.stringify(await r.json(),null,2)}loadSpec();</script>"
                    + "</body></html>";
            write(exchange, 200, html, "text/html; charset=utf-8");
            return;
        }
        Router.MatchedRoute matched = router.match(exchange.getRequestMethod(), path);
        byte[] response; int status; String contentType = "text/plain; charset=utf-8";
        if (matched == null) { response = "Not Found".getBytes(StandardCharsets.UTF_8); status = 404; }
        else if (matched.route().handler() == null) { response = "Zupix route matched".getBytes(StandardCharsets.UTF_8); status = 200; }
        else {
            try {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                Object result = matched.route().handler().invoke(matched.parameters(), exchange.getRequestURI().getRawQuery(), body);
                if (result instanceof Response explicit) { status = explicit.status(); applyHeaders(exchange, explicit.headers()); result = explicit.body(); }
                else status = 200;
                if (result == null) response = new byte[0];
                else if (result instanceof String text) response = text.getBytes(StandardCharsets.UTF_8);
                else { response = Json.write(result).getBytes(StandardCharsets.UTF_8); contentType = "application/json; charset=utf-8"; }
            } catch (ValidationException exception) {
                response = Json.write(java.util.Map.of("detail", exception.errors())).getBytes(StandardCharsets.UTF_8); status = 422; contentType = "application/json; charset=utf-8";
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
    private static void write(HttpExchange exchange, int status, String body, String contentType) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream output = exchange.getResponseBody()) { output.write(bytes); }
    }
    private static void applyHeaders(HttpExchange exchange, java.util.Map<String, String> headers) { headers.forEach((name, value) -> exchange.getResponseHeaders().set(name, value)); }
}
