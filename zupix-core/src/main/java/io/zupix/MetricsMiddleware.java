package io.zupix;

import java.util.Objects;

/** Records request count, failures and elapsed time without imposing a metrics backend. */
public final class MetricsMiddleware implements Middleware {
    private final Metrics metrics;

    public MetricsMiddleware(Metrics metrics) { this.metrics = Objects.requireNonNull(metrics, "metrics"); }
    public Metrics metrics() { return metrics; }

    @Override
    public void handle(RequestContext request, MiddlewareChain chain) {
        long started = System.nanoTime();
        boolean failure = false;
        try { chain.next(); }
        catch (RuntimeException exception) { failure = true; throw exception; }
        finally { metrics.record(System.nanoTime() - started, failure); }
    }
}
