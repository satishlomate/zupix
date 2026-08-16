# Zupix

### A FastAPI-inspired, modern Java API framework

**Zupix** is an open-source Java framework focused on making API development simple, fast, type-safe, and enjoyable.

> **Write less. Build faster. Ship production-ready Java APIs.**

Created and maintained by **Satish Lomate**.

## 🚧 Project Status

**Pre-alpha — v0.1.0 stabilization**

Zupix is an early-stage framework. Public APIs and internals may still change before a stable 1.0 release.

The current branch focuses on making the core runtime reliable before expanding the feature set.

## ✨ Current Foundation

- Java 21 baseline with virtual-thread HTTP runtime
- Annotation-based routing: `@Get`, `@Post`, `@Put`, `@Patch`, `@Delete`
- Path and query parameter binding
- JSON request/response handling
- Jakarta Validation integration
- Middleware pipeline
- Exception handling
- Dependency injection foundation
- Configuration and profiles
- Request headers and bearer authentication infrastructure
- Role-based authorization with `@RolesAllowed`
- CORS configuration foundation
- Logging and request metrics foundations
- OpenAPI document generation foundation
- `zupix new` and `zupix run` CLI foundation
- Automated Maven/JUnit integration tests
- GitHub Actions CI
- Apache-2.0 licensing

## 🚀 Quick Start

The intended developer experience is:

```bash
zupix new my-api
cd my-api
zupix run
```

Example application:

```java
import io.zupix.*;

@ZupixApp
public class Application {

    @Get("/")
    public String hello() {
        return "Hello Zupix!";
    }
}
```

## 🏗️ Architecture

Zupix is being developed as a modular platform:

```text
Zupix
├── zupix-core
├── zupix-security
├── zupix-cli
└── future modules
    ├── zupix-http
    ├── zupix-router
    ├── zupix-json
    ├── zupix-validation
    ├── zupix-openapi
    └── zupix-test
```

The current implementation deliberately keeps the stable core small. Additional capabilities should become separate modules when doing so improves dependency isolation and API stability.

## 🧪 Testing

The project uses JUnit 5 and HTTP-level integration tests.

Run the full verification locally:

```bash
mvn -B clean verify
```

The CI pipeline runs the same verification on Java 21 for every push and pull request.

## 📦 Technology

- Java 21+
- Maven
- JUnit 5
- Jackson
- Jakarta Validation
- Java virtual threads

Dependencies and implementation details may evolve during pre-alpha development.

## 🗺️ Roadmap

### v0.1.0 — Stabilization

- [x] Multi-module Maven project
- [x] Java 21 baseline
- [x] HTTP server
- [x] Router
- [x] Basic HTTP annotations
- [x] JSON serialization
- [x] Path/query binding
- [x] Validation
- [x] Middleware
- [x] Exception handling
- [x] Dependency injection foundation
- [x] Authentication/authorization foundation
- [x] Metrics foundation
- [x] CLI foundation
- [x] CI pipeline
- [ ] Final end-to-end release audit
- [ ] Performance baseline
- [ ] Security review
- [ ] Release candidate documentation

### Future

- [ ] Complete OpenAPI schema generation
- [ ] Swagger UI / ReDoc integration
- [ ] Development mode and hot reload where practical
- [ ] First-class testing utilities
- [ ] CORS runtime completion
- [ ] Production observability
- [ ] Optional JWT provider
- [ ] Maven Central publication
- [ ] API compatibility policy
- [ ] `1.0.0` stable release

## 🔐 Security

Security features are designed to remain modular. Zupix core provides authentication and authorization contracts; cryptographic token verification should be supplied by a dedicated security provider rather than embedded into the core runtime.

Do not treat the pre-alpha release as production-ready without an application-level security review.

## 📄 License

Zupix is released under the **Apache License 2.0**.

Copyright © 2026 Satish Lomate.

## 👨‍💻 Creator

**Satish Lomate**

Zupix is an independent open-source project created and maintained by Satish Lomate.

## ⭐ Goal

Make Java API development as simple and enjoyable as possible — with a FastAPI-like developer experience and a native modern-Java architecture.
