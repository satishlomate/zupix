package io.zupix;

import java.util.concurrent.atomic.LongAdder;

/** Lightweight in-process request metrics for development and diagnostics. */
public final class Metrics {
    private final LongAdder requests = new LongAdder();
    private final LongAdder failures = new LongAdder();
    private final LongAdder totalNanos = new LongAdder();

    void record(long elapsedNanos, boolean failure) {
        requests.increment();
        totalNanos.add(elapsedNanos);
        if (failure) failures.increment();
    }

    public long requests() { return requests.sum(); }
    public long failures() { return failures.sum(); }
    public long totalNanos() { return totalNanos.sum(); }
    public double averageMillis() { long count = requests(); return count == 0 ? 0.0 : (totalNanos() / 1_000_000.0) / count; }
}
