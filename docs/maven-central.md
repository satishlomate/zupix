# Publishing Zupix to Maven Central

Zupix uses Sonatype Central Publisher Portal and the `central-publishing-maven-plugin`.

## Published components

The release build publishes the library components:

- `io.zupix:zupix-parent`
- `io.zupix:zupix-core`
- `io.zupix:zupix-http`
- `io.zupix:zupix-security`
- `io.zupix:zupix-test`

`zupix-cli` and `hello-world` are intentionally excluded from Central publication.

## One-time Sonatype setup

1. Create/sign in to the Central Portal.
2. Register and verify the `io.zupix` namespace by proving control of `zupix.io` with the TXT record supplied by Central.
3. Create a Central Portal user token.
4. Create a GPG/PGP signing key and publish the public key to a supported key server.

Central requires sources and Javadoc JARs, GPG/PGP signatures, required POM metadata, and checksums for published components. The repository build generates the sources, Javadocs, and signatures; the Central publishing plugin generates checksums for the staged bundle.

## GitHub Actions secrets

Configure these repository secrets before creating a release tag:

- `MAVEN_USERNAME` — Central Portal user-token username.
- `MAVEN_CENTRAL_TOKEN` — Central Portal user-token password/token.
- `MAVEN_GPG_PRIVATE_KEY` — ASCII-armored transferable secret key, for example from `gpg --armor --export-secret-keys <KEY_ID>`.
- `MAVEN_GPG_PASSPHRASE` — passphrase for the signing key.

Never commit credentials, private keys, `settings.xml` credentials, or passphrases to the repository.

## Local publishing

Configure `~/.m2/settings.xml` with the Central user token:

```xml
<settings>
  <servers>
    <server>
      <id>central</id>
      <username>YOUR_TOKEN_USERNAME</username>
      <password>YOUR_TOKEN_PASSWORD</password>
    </server>
  </servers>
</settings>
```

Make sure the signing key is available to GPG, then run:

```bash
mvn -B -P release clean verify
mvn -B -P release deploy
```

The repository is configured with `autoPublish=false`, so the deployment is uploaded to Central for validation and manual publication. This is intentional for the first release so the validation results can be inspected before publication.

## GitHub release

Creating a tag such as `v0.1.0` triggers `.github/workflows/release.yml`.

The workflow:

1. Installs Java 21.
2. Configures Maven Central credentials.
3. Imports the signing key.
4. Runs `mvn -B -P release clean verify`.
5. Runs `mvn -B -P release deploy`.
6. Creates a GitHub release containing the library JAR artifacts.

Do not create the first release tag until the `io.zupix` namespace is verified and all four GitHub Actions secrets are configured.
