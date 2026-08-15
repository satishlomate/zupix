# Releasing Zupix

Zupix is currently pre-alpha. Releases must be tagged explicitly and verified by CI.

## Version

Use semantic version tags such as:

```text
v0.1.0
v0.2.0
v1.0.0
```

## Local verification

```bash
mvn -B clean verify
mvn -B package -DskipTests
```

## GitHub release flow

1. Ensure CI is green on `feature/zupix-foundation`.
2. Merge the release changes into the release branch/main branch when ready.
3. Create an annotated tag such as `v0.1.0`.
4. GitHub Actions runs `.github/workflows/release.yml`.
5. Review the generated JAR artifacts before publishing them.

## Maven Central

Do not publish credentials in the repository. Maven Central credentials and signing keys must be stored as GitHub Actions secrets before an automated Central publishing job is enabled.

The first public release should be treated as a deliberate compatibility milestone, not merely a build artifact.
