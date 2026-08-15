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
    private final MiddlewareRegistry middleware;
    private final ExceptionRegistry exceptions;

    private ZupixServer(HttpServer server, Router router, MiddlewareRegistry middleware, ExceptionRegistry exceptions) {
        this.server = server;
        this.router = router;
        this.middleware = middleware;
        this.exceptions = exceptions;
    }

    public static ZupixServer create(int port, Router router) throws IOException {
        return create(port, router, new MiddlewareRegistry(), new ExceptionRegistry());
    }

    public static ZupixServer create(int port, Router router, MiddlewareRegistry middleware,
                                     ExceptionRegistry exceptions) throws IOException {
        Objects.requireNonNull(router, "router");
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        ZupixServer runtime = new ZupixServer(server, router, middleware, exceptions);
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
                    + "<script>async function loadSpec(){const r=await fetch('/openapi.json');document.getElementById('spec').textContent=JSON.stringify(await r.json(),null,2)}loadSpec();</script>"
                    + "</body></html>";
            write(exchange, 200, html, "text/html; charset=utf-8");
            return;
        }

        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        RequestContext request = new RequestContext(exchange.getRequestMethod(), path,
                exchange.getRequestURI().getRawQuery(), body);
        final HttpResponse pipelineResponse = new HttpResponse();
        try {
            new MiddlewarePipeline(middleware.all()).execute(request, () -> {
                try {
                    pipelineResponse.set(dispatch(exchange, body));
                } catch (RuntimeException exception) {
                    Response handled = exceptions.handle(exception);
                    if (handled != null) pipelineResponse.set(handled);
                    else throw exception;
                }
            });
        } catch (RuntimeException exception) {
            Response handled = exceptions.handle(exception);
            pipelineResponse.set(handled != null ? handled : Response.status(500, "Internal Server Error"));
        }

        Response result = pipelineResponse.get();
        if (result == null) result = Response.status(500, "No response produced");
        writeResponse(exchange, result);
    }

    private Response dispatch(HttpExchange exchange, String body) {
        Router.MatchedRoute matched = router.match(exchange.getRequestMethod(), exchange.getRequestURI().getPath());
        if (matched == null) return Response.status(404, "Not Found");
        if (matched.route().handler() == null) return Response.ok("Zupix route matched");
        try {
            Object result = matched.route().handler().invoke(matched.parameters(), exchange.getRequestURI().getRawQuery(), body);
            return result instanceof Response explicit ? explicit : Response.ok(result);
        } catch (ValidationException exception) {
            return Response.status(422, java.util.Map.of("detail", exception.errors()));
        } catch (IllegalArgumentException exception) {
            return Response.status(400, exception.getMessage());
        }
    }

    private static void writeResponse(HttpExchange exchange, Response result) throws IOException {
        applyHeaders(exchange, result.headers());
        Object body = result.body();
        String contentType;
        byte[] bytes;
        if (body == null) {
            bytes = new byte[0]; contentType = "text/plain; charset=utf-8";
        } else if (body instanceof String text) {
            bytes = text.getBytes(StandardCharsets.UTF_8); contentType = "text/plain; charset=utf-8";
        } else {
            bytes = Json.write(body).getBytes(StandardCharsets.UTF_8); contentType = "application/json; charset=utf-8";
        }
        if (exchange.getResponseHeaders().getFirst("Content-Type") == null) exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(result.status(), bytes.length);
        try (OutputStream output = exchange.getResponseBody()) { output.write(bytes); }
    }

    private static void write(HttpExchange exchange, int status, String body, String contentType) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream output = exchange.getResponseBody()) { output.write(bytes); }
    }

    private static void applyHeaders(HttpExchange exchange, java.util.Map<String, String> headers) {
        headers.forEach((name, value) -> exchange.getResponseHeaders().set(name, value));
    }
}
