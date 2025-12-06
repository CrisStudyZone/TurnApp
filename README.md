# TurnApp build notes

This repository currently holds the shared Gradle configuration for the Android project. The
`gradle/libs.versions.toml` catalog pins the toolchain to Kotlin **2.2.0** so it matches
libraries that were already compiled with the 2.2 metadata (e.g., Compose 2.8.0 artifacts and
`com.kizitonwose.calendar:core:2.8.0`).

## Fixing the Kotlin metadata mismatch
If you see an error like `Module was compiled with an incompatible version of Kotlin. The binary
version of its metadata is 2.2.0, expected version is 2.0.0`, it means the project is building
with a Kotlin compiler older than the artifacts on disk.

**To upgrade the project to Kotlin 2.2 (recommended):**
1. Keep the versions defined in `gradle/libs.versions.toml` (Kotlin 2.2.0, KSP 2.2.0-1.0.24,
   AGP 8.7.2) and ensure Android Studio uses the matching Kotlin plugin.
2. For existing modules, make sure the Compose compiler/extension version you declare is
   compatible with Kotlin 2.2.
3. Refresh dependencies and rebuild: `./gradlew --refresh-dependencies clean assembleDebug`.

**If you must stay on Kotlin 2.0:**
1. Downgrade dependencies compiled for 2.2 (e.g., use an older Compose/Kizitonwose version).
2. Adjust the Kotlin/KSP versions in `gradle/libs.versions.toml` back to a 2.0.x release.
3. Clear caches: delete `~/.gradle/caches` or use `./gradlew clean` before rebuilding.

## Next steps
Once the `app/` module is added back to version control, point it at the version catalog
and ensure its plugins use the `alias(libs.plugins.…)` entries already declared in
`build.gradle.kts`.

See [`docs/production-plan.md`](docs/production-plan.md) for a prioritized checklist to harden the
project for production (CI, quality gates, dependency locking, and signing guidance).
