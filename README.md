# Zupix

### A FastAPI-inspired, modern Java API framework

Zupix is an open-source Java framework focused on making API development simple, fast, type-safe, and enjoyable.

> **Write less. Build faster. Ship production-ready Java APIs.**

Created and maintained by **Satish Lomate**.

## 🚧 Project Status

**Pre-alpha — v0.1.0 stabilization**

Public APIs and internals may still change before a stable 1.0 release.

## ✨ Current Foundation

- Java 21 baseline with virtual-thread HTTP runtime
- Annotation-based routing: `@Get`, `@Post`, `@Put`, `@Patch`, `@Delete`
- Path and query parameter binding
- JSON request/response handling
- Jakarta Validation integration
- Middleware pipeline and exception handling
- Constructor-based dependency injection
- Request headers and authentication context
- Role-based authorization with `@RolesAllowed`
- CORS configuration/runtime handling
- Request metrics foundation
- OpenAPI document generation and `/openapi.json`
- `/docs` endpoint
- `zupix new` and `zupix run` CLI
- HTTP testing utilities
- JUnit 5 and HTTP-level integration tests
- GitHub Actions CI with dependency review
- Apache-2.0 licensing

## 🚀 Quick Start

```bash
zupix new my-api
cd my-api
zupix run
```

Example:

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

```text
Zupix
├── zupix-core       # routing, binding, JSON, validation, DI, security contracts
├── zupix-http       # HTTP runtime and application lifecycle
├── zupix-security   # security integration module
├── zupix-test       # testing utilities
├── zupix-cli        # project creation and run commands
└── examples
    └── hello-world
```

The module boundaries are intentionally pragmatic during pre-alpha. A capability remains in core when extracting it would create unnecessary dependency coupling.

## 🧪 Testing

Run the full verification locally:

```bash
mvn -B clean verify
```

CI runs the same verification on Java 21 for pushes and pull requests.

## 📦 Technology

- Java 21+
- Maven
- JUnit 5
- Jackson
- Jakarta Validation
- Java virtual threads

## 🗺️ Roadmap

### v0.1.0 — Stabilization

- [x] Multi-module Maven project
- [x] Java 21 baseline
- [x] HTTP server
- [x] Router
- [x] HTTP annotations
- [x] JSON serialization
- [x] Path/query binding
- [x] Validation
- [x] Middleware
- [x] Exception handling
- [x] Dependency injection foundation
- [x] Authentication/authorization foundation
- [x] CORS runtime
- [x] Metrics foundation
- [x] OpenAPI generation foundation
- [x] CLI foundation
- [x] Testing utilities
- [x] CI pipeline
- [ ] End-to-end release audit
- [ ] Performance baseline
- [ ] Security review
- [ ] Release-candidate documentation

### Future

- [ ] Swagger UI / ReDoc integration
- [ ] Development mode and hot reload where practical
- [ ] Production observability
- [ ] Optional JWT provider
- [ ] Maven Central publication
- [ ] API compatibility policy
- [ ] `1.0.0` stable release

## 🔐 Security

Zupix provides pluggable authentication and role-authorization contracts. Cryptographic token verification should be supplied by a dedicated provider rather than embedded into the core runtime.

The pre-alpha release should receive an application-level security review before production use.

## 📄 License

Zupix is released under the **Apache License 2.0**.

Copyright © 2026 Satish Lomate.

## ⭐ Goal

Make Java API development as simple and enjoyable as possible — with a FastAPI-like developer experience and a native modern-Java architecture.
