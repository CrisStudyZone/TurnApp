# Production hardening plan

The repository currently only contains shared Gradle configuration. The checklist below captures
high-impact tasks to make the project buildable, testable, and releasable in production.

## Stabilize the build
- **Version control the app module**: add `app/` with its `build.gradle.kts`, manifests, and
  sources. Point plugin declarations to the version catalog aliases already defined at the root.
- **Align Kotlin toolchain**: keep the Kotlin and KSP versions in `gradle/libs.versions.toml`
  consistent with the IDE plugin. Pin a Compose Compiler version that matches Kotlin 2.2.
- **Lock dependency resolutions**: enable Gradle's dependency verification or use a
  `gradle/dependency-locks` setup to avoid accidental upgrades in CI.
- **Configure Java toolchain**: set `java { toolchain { languageVersion.set(JavaLanguageVersion.of(17)) } }`
  in the Android module to ensure reproducible builds across machines.

## Quality and safety gates
- **Static analysis**: add Detekt and ktlint tasks, and wire them into CI. Keep baseline files in
  the repo to avoid flaky results.
- **Testing**: introduce unit tests and instrumented tests; ensure CI runs `./gradlew test`
  and `connectedCheck` (when an emulator is available).
- **Lint and R8**: enable Android Lint in CI and verify that release builds run with code
  shrinking/obfuscation and resource shrinking enabled.

## Delivery pipeline
- **CI workflow**: create a GitHub Actions workflow that caches Gradle, runs lint/tests, and builds
  debug and release artifacts. Include a `./gradlew doctor` or `--scan` option for diagnostics when
  failing.
- **Artifact signing**: document how signing configs and keystores are provided (e.g., via
  encrypted secrets in CI) without committing them to the repo.
- **Versioning and changelog**: adopt semantic versioning and maintain a `CHANGELOG.md`; automate
  version bumps via Gradle properties or a release script.

## Developer experience
- **Pre-commit hooks**: add a script (e.g., `./gradlew lintKotlin detekt`) to run locally before
  pushing. Optionally wire up Git hooks via a tool like `pre-commit`.
- **Caching**: enable the Gradle configuration cache and build cache in `gradle.properties` once
  the modules are in place and compatible.
- **Documentation**: expand `README.md` with module structure, supported SDK versions, and how to
  reproduce builds locally (Java version, Android Studio version).

Keeping these items tracked will help move the project from shared configuration to a
production-ready Android application.
