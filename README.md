# Zupix

### A FastAPI-inspired, modern Java API framework

**Zupix** is an open-source Java framework focused on making API development simple, fast, type-safe, and enjoyable.

> **Write less. Build faster. Ship production-ready Java APIs.**

Created and maintained by **Satish Lomate**.

---

## 🚧 Project Status

**Early Development — Pre-alpha**

Zupix is currently being built from the ground up. APIs and internal architecture may change before the first stable release.

The initial goal is to deliver a FastAPI-style developer experience for modern Java without trying to reproduce the complexity of traditional enterprise frameworks.

---

## 🎯 Vision

Zupix aims to make building a Java API as simple as:

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

The framework should provide sensible defaults so developers can focus on application logic instead of configuration and boilerplate.

---

## ✨ Planned Features

### Core API

- Annotation-based routing
- `@Get`, `@Post`, `@Put`, `@Patch`, and `@Delete`
- Path parameters
- Query parameters
- Headers and cookies
- Request and response objects
- JSON request/response handling
- Automatic content negotiation

### Developer Experience

- Minimal configuration
- Fast startup
- Simple project creation CLI
- Development mode
- Hot reload where practical
- Clear error messages
- First-class documentation

### Type Safety & Validation

- Java records support
- Request model validation
- Jakarta Validation integration
- Typed path/query/body parameters
- Automatic validation errors

### API Documentation

- Automatic OpenAPI generation
- Swagger UI
- ReDoc
- `/openapi.json`
- Documentation generated from Java types and annotations

### Application Infrastructure

- Dependency injection
- Middleware
- Exception handling
- Configuration
- Profiles
- Logging
- Metrics

### Modern Java

Zupix targets modern Java and is designed to take advantage of capabilities such as:

- Java 21+
- Virtual Threads
- Records
- Modern concurrency APIs
- Strong typing

---

## 🏗️ Architecture

The framework is designed as a modular platform:

```text
Zupix
├── zupix-core
├── zupix-http
├── zupix-router
├── zupix-di
├── zupix-json
├── zupix-validation
├── zupix-openapi
├── zupix-security
├── zupix-test
├── zupix-cli
└── examples
```

Modules will remain independently testable and will be added incrementally.

---

## 🚀 Developer Experience Goal

A new developer should be able to create and run an API in minutes:

```bash
zupix new my-api
cd my-api
zupix run
```

Then access:

```text
http://localhost:8080
http://localhost:8080/docs
http://localhost:8080/openapi.json
```

The exact CLI and APIs are subject to change during early development.

---

## 🔥 Why Zupix?

Zupix is designed around five principles:

1. **Simple** — minimal boilerplate.
2. **Fast** — fast startup and efficient request handling.
3. **Type-safe** — use Java's type system instead of repetitive configuration.
4. **Observable** — documentation, errors, logs, and metrics should be easy to understand.
5. **Production-minded** — security, testing, validation, and maintainability are part of the design from the beginning.

Zupix is inspired by the developer experience of modern frameworks such as FastAPI, while remaining a native Java framework with its own architecture and APIs.

---

## 🧪 Testing Philosophy

Every major framework feature should have automated tests before being considered stable.

The project will use:

- Unit tests
- Integration tests
- HTTP-level tests
- API compatibility tests
- Performance benchmarks

Performance claims will be benchmarked rather than assumed.

---

## 📦 Technology Direction

The initial implementation is planned around:

- **Java 21+**
- **Netty** for the HTTP server layer
- **Jackson** for JSON
- **Jakarta Validation** for validation
- **JUnit 5** for testing
- **Maven** for build and dependency management

Dependencies and implementation details may evolve as the project matures.

---

## 🗺️ Roadmap

### Phase 1 — Foundation

- [ ] Multi-module Maven project
- [ ] Java 21 baseline
- [ ] Core application lifecycle
- [ ] HTTP server
- [ ] Router
- [ ] Basic annotations
- [ ] JSON serialization

### Phase 2 — FastAPI-style API Layer

- [ ] Path parameters
- [ ] Query parameters
- [ ] Request body binding
- [ ] Response models
- [ ] Validation
- [ ] Exception handling
- [ ] Dependency injection

### Phase 3 — Documentation

- [ ] OpenAPI generator
- [ ] Swagger UI
- [ ] ReDoc
- [ ] Automatic schema generation

### Phase 4 — Developer Tools

- [ ] `zupix new`
- [ ] `zupix run`
- [ ] Development mode
- [ ] Testing utilities
- [ ] Project templates

### Phase 5 — Production Readiness

- [ ] Security
- [ ] CORS
- [ ] Authentication
- [ ] Authorization
- [ ] Metrics
- [ ] Observability
- [ ] Performance benchmarks
- [ ] Security review

### Phase 6 — First Stable Release

- [ ] Complete documentation
- [ ] Compatibility policy
- [ ] API stability review
- [ ] Maven Central publication
- [ ] Apache-2.0 licensing
- [ ] `1.0.0` release

---

## 🤝 Contributing

Zupix is intended to become a community-driven open-source project.

During the pre-alpha period, architecture and public APIs are expected to evolve quickly. Contributions, design discussions, bug reports, documentation improvements, and performance experiments will be welcomed as the project becomes ready for broader participation.

Contribution guidelines will be added before the first public contributor release.

---

## 📄 License

Zupix is planned to be released under the **Apache License 2.0**.

Copyright © 2026 Satish Lomate.

The final license files and legal notices will be added before the first public release.

---

## 👨‍💻 Creator

**Satish Lomate**

Zupix is an independent open-source project created and maintained by Satish Lomate.

---

## ⭐ Project Goal

The long-term goal is simple:

> **Make Java API development as simple and enjoyable as possible.**

If Zupix can let a developer go from a blank project to a documented, tested API in a few minutes, the project is succeeding.
