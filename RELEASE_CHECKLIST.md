# Zupix v0.1.0 Release Checklist

## Build

- [ ] `mvn -B clean verify` passes on Java 21
- [ ] GitHub Actions build is green
- [ ] Dependency review passes

## Runtime

- [ ] Basic GET route works over HTTP
- [ ] 404 behavior verified
- [ ] Middleware and exception handling verified
- [ ] JSON request/response verified
- [ ] Validation behavior verified
- [ ] Authentication and authorization verified
- [ ] CORS behavior verified
- [ ] `/openapi.json` verified
- [ ] `/docs` verified

## CLI

- [ ] `zupix new <name>` creates a buildable project
- [ ] Generated application starts
- [ ] `zupix run` starts the generated application
- [ ] `zupix version` reports `0.1.0`

## Security

- [ ] Dependency vulnerabilities reviewed
- [ ] Authentication paths reviewed
- [ ] Authorization paths reviewed
- [ ] Error responses checked for sensitive information
- [ ] CORS configuration reviewed

## Performance

- [ ] Benchmark harness executed
- [ ] Environment and JVM recorded
- [ ] Startup and request latency baseline recorded

## Publishing

- [ ] Apache-2.0 license present
- [ ] Sources JAR generated
- [ ] Javadocs JAR generated
- [ ] POM metadata complete
- [ ] Signing credentials configured
- [ ] Maven Central publishing configured
- [ ] Release tag prepared

Do not publish `v0.1.0` until the required verification items above are complete.
