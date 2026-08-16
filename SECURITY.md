# Security Policy

## Scope

Zupix is currently pre-alpha / release-candidate development software. Public APIs and internal implementation details may still change.

## Reporting a vulnerability

Please do not disclose exploitable security vulnerabilities in public issues. Until a dedicated security contact is published, report security concerns privately to the project maintainer through the GitHub repository owner.

Include:

- affected version or commit
- affected module
- reproduction steps
- security impact
- suggested mitigation, if known

Do not include secrets or private user data in reports.

## Security design

Authentication and authorization are intentionally modular. Core Zupix provides pluggable authentication and role authorization; applications are responsible for selecting and securely configuring credential verification.

JWT or other cryptographic token verification should be supplied by a dedicated security module/provider rather than implemented as ad-hoc cryptography in application code.

## Release security checklist

Before a stable release:

- review authentication and authorization paths
- test unauthenticated and unauthorized requests
- review dependency vulnerabilities
- verify CORS configuration behavior
- verify exception responses do not leak sensitive details
- run the full test suite
