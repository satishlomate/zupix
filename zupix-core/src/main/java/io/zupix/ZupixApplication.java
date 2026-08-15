package io.zupix;

import java.io.IOException;
import java.util.Objects;

/** Entry point and lifecycle facade for a Zupix application. */
public final class ZupixApplication implements AutoCloseable {
    private final Router router;
    private final ZupixServer server;

    private ZupixApplication(Router router, ZupixServer server) {
        this.router = router;
        this.server = server;
    }

    /**
     * Creates a Zupix application by discovering routes on the supplied API object.
     */
    public static ZupixApplication create(Object api, int port) throws IOException {
        Objects.requireNonNull(api, "api");
        Router router = new Router();
        for (RouteScanner.DiscoveredRoute discovered : new RouteScanner().scan(api)) {
            router.add(discovered.route(), discovered.handler());
        }
        return new ZupixApplication(router, ZupixServer.create(port, router));
    }

    public void start() {
        server.start();
    }

    public int port() {
        return server.port();
    }

    public Router router() {
        return router;
    }

    @Override
    public void close() {
        server.close();
    }
}
