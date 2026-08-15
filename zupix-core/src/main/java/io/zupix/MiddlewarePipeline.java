package io.zupix;

import java.util.List;

/** Executes registered middleware in order and finishes at the route handler. */
final class MiddlewarePipeline {
    private final List<Middleware> middlewares;

    MiddlewarePipeline(List<Middleware> middlewares) {
        this.middlewares = List.copyOf(middlewares);
    }

    void execute(RequestContext request, Runnable endpoint) {
        invoke(0, request, endpoint);
    }

    private void invoke(int index, RequestContext request, Runnable endpoint) {
        if (index >= middlewares.size()) {
            endpoint.run();
            return;
        }
        Middleware middleware = middlewares.get(index);
        middleware.handle(request, () -> invoke(index + 1, request, endpoint));
    }
}
