package io.zupix;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MetricsTest {
    @Test
    void recordsRequestsAndFailures() {
        var metrics = new Metrics();
        metrics.record(1_000_000, false);
        metrics.record(3_000_000, true);

        assertEquals(2, metrics.requests());
        assertEquals(1, metrics.failures());
        assertEquals(2.0, metrics.averageMillis());
    }
}
