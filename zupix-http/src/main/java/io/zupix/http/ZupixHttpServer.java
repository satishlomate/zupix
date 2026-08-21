package io.zupix.http;

import io.zupix.CorsMiddleware;
import io.zupix.ExceptionRegistry;
import io.zupix.ForbiddenException;
import io.zupix.HttpResponse;
import io.zupix.Json;
import io.zupix.Middleware;
import io.zupix.MiddlewarePipeline;
import io.zupix.MiddlewareRegistry;
import io.zupix.OpenApiGenerator;
import io.zupix.RequestContext;
import io.zupix.Response;
import io.zupix.Router;
import io.zupix.ValidationException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;

/** HTTP runtime adapter for Zupix. */
public final class ZupixHttpServer implements AutoCloseable {
    private final HttpServer server;
    private final Router router;
    private final MiddlewareRegistry middleware;
    private final ExceptionRegistry exceptions;

    private ZupixHttpServer(HttpServer server, Router router,
                            MiddlewareRegistry middleware,
                            ExceptionRegistry exceptions) {
        this.server = server;
        this.router = router;
        this.middleware = middleware;
        this.exceptions = exceptions;
    }

    public static ZupixHttpServer create(int port, Router router,
                                         MiddlewareRegistry middleware,
                                         ExceptionRegistry exceptions) throws IOException {
        Objects.requireNonNull(router, "router");
        Objects.requireNonNull(middleware, "middleware");
        Objects.requireNonNull(exceptions, "exceptions");
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        ZupixHttpServer runtime = new ZupixHttpServer(server, router, middleware, exceptions);
        server.createContext("/", runtime::handle);
        server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        return runtime;
    }

    public void start() { server.start(); }
    public int port() { return server.getAddress().getPort(); }
    @Override public void close() { server.stop(0); }

    private void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Map<String, String> headers = new LinkedHashMap<>();
        exchange.getRequestHeaders().forEach((name, values) -> {
            if (!values.isEmpty()) headers.put(name, values.get(0));
        });
        RequestContext request = new RequestContext(exchange.getRequestMethod(), path,
                exchange.getRequestURI().getRawQuery(), headers, body);
        HttpResponse state = new HttpResponse();

        CorsMiddleware cors = findCors();
        if (cors != null) {
            applyCors(exchange, cors);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                writeResponse(exchange, Response.status(204, null));
                return;
            }
        }

        try {
            new MiddlewarePipeline(middleware.all()).execute(request, () -> {
                try { state.set(dispatch(exchange, body)); }
                catch (RuntimeException e) {
                    Response handled = exceptions.handle(e);
                    if (handled != null) state.set(handled); else throw e;
                }
            });
        } catch (RuntimeException e) {
            Response handled = exceptions.handle(e);
            state.set(handled != null ? handled : Response.status(500, "Internal Server Error"));
        }
        writeResponse(exchange, state.get() == null
                ? Response.status(500, "No response produced") : state.get());
    }

    private CorsMiddleware findCors() {
        for (Middleware item : middleware.all()) if (item instanceof CorsMiddleware cors) return cors;
        return null;
    }

    private static void applyCors(HttpExchange exchange, CorsMiddleware cors) {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", cors.allowOrigin());
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", cors.allowMethodsHeader());
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", cors.allowHeadersHeader());
    }

    private Response dispatch(HttpExchange exchange, String body) {
        String path = exchange.getRequestURI().getPath();
        if ("GET".equalsIgnoreCase(exchange.getRequestMethod()) && "/openapi.json".equals(path))
            return Response.ok(new OpenApiGenerator().generate(router));
        if ("GET".equalsIgnoreCase(exchange.getRequestMethod()) && "/docs".equals(path))
            return Response.ok("<!doctype html><html><head><meta charset=\"utf-8\"><title>Zupix API Docs</title></head><body><h1>Zupix API</h1><p>OpenAPI: <a href=\"/openapi.json\">/openapi.json</a></p></body></html>");

        Router.MatchedRoute matched = router.match(exchange.getRequestMethod(), path);
        if (matched == null) return Response.status(404, "Not Found");
        try {
            if (matched.route().handler() == null) return Response.ok("Zupix route matched");
            Object result = matched.route().handler().invoke(matched.parameters(),
                    exchange.getRequestURI().getRawQuery(), body);
            return result instanceof Response r ? r : Response.ok(result);
        } catch (ForbiddenException e) {
            return Response.status(403, e.getMessage());
        } catch (ValidationException e) {
            return Response.status(422, Map.of("detail", e.errors()));
        } catch (IllegalArgumentException e) {
            return Response.status(400, e.getMessage());
        }
    }

    private static void writeResponse(HttpExchange exchange, Response result) throws IOException {
        result.headers().forEach((name, value) -> exchange.getResponseHeaders().set(name, value));
        Object body = result.body();
        String type;
        byte[] bytes;
        if (body == null) {
            bytes = new byte[0]; type = "text/plain; charset=utf-8";
        } else if (body instanceof String text) {
            bytes = text.getBytes(StandardCharsets.UTF_8); type = "text/plain; charset=utf-8";
        } else {
            bytes = Json.write(body).getBytes(StandardCharsets.UTF_8); type = "application/json; charset=utf-8";
        }
        if (exchange.getResponseHeaders().getFirst("Content-Type") == null)
            exchange.getResponseHeaders().set("Content-Type", type);
        exchange.sendResponseHeaders(result.status(), bytes.length);
        try (OutputStream output = exchange.getResponseBody()) { output.write(bytes); }
    }
}
