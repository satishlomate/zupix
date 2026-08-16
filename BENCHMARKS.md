# Zupix Benchmark Baseline

Zupix performance claims must be measured rather than assumed.

## Initial benchmark goals

Measure at minimum:

- application startup time
- requests per second for a simple GET route
- median and p95 latency
- JSON serialization/deserialization throughput
- routing overhead with 1, 10, 100, and 1000 routes

## Method

Use a dedicated benchmark environment with Java 21. Record:

- CPU model
- OS
- JVM version
- Zupix commit/version
- request concurrency
- payload size
- warmup duration
- measurement duration

Do not publish performance numbers until the benchmark is repeatable and includes a comparison methodology.

## v0.1.0

The first release should establish the benchmark harness and methodology. Absolute performance targets are intentionally not claimed yet.
