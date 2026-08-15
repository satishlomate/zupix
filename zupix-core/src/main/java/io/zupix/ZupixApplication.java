package io.zupix;

import java.io.IOException;
import java.util.Objects;

/** Entry point and lifecycle facade for a Zupix application. */
public final class ZupixApplication implements AutoCloseable {
    private final Router router;
    private final MiddlewareRegistry middleware;
    private final ExceptionRegistry exceptions;
    private final ZupixServer server;

    private ZupixApplication(Router router, MiddlewareRegistry middleware,
                             ExceptionRegistry exceptions, ZupixServer server) {
        this.router = router;
        this.middleware = middleware;
        this.exceptions = exceptions;
        this.server = server;
    }

    public static ZupixApplication create(Object api, int port) throws IOException {
        Objects.requireNonNull(api, "api");
        Router router = new Router();
        for (RouteScanner.DiscoveredRoute discovered : new RouteScanner().scan(api)) {
            router.add(discovered.route(), discovered.handler());
        }
        MiddlewareRegistry middleware = new MiddlewareRegistry();
        ExceptionRegistry exceptions = new ExceptionRegistry();
        return new ZupixApplication(router, middleware, exceptions,
                ZupixServer.create(port, router, middleware, exceptions));
    }

    public ZupixApplication use(Middleware value) {
        middleware.use(Objects.requireNonNull(value, "middleware"));
        return this;
    }

    public <T extends Throwable> ZupixApplication on(Class<T> type, ExceptionHandler<T> handler) {
        exceptions.on(type, handler);
        return this;
    }

    public void start() { server.start(); }
    public int port() { return server.port(); }
    public Router router() { return router; }

    @Override
    public void close() { server.close(); }
}
