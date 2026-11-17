# HiAnime & AnimePahe extensions for Aniyomi

This repository contains Android extension modules for Aniyomi. It is **not** a Cloudstream repository: the output is installable Aniyomi APKs, not `.cs3` plugins.

## Sources

| Extension | Website | Status |
| --- | --- | --- |
| HiAnime | https://hianime.ms | Under development; the domain is intentionally kept on `hianime.ms`. |
| AnimePahe | https://animepahe.pw | Module scaffold only; provider implementation is pending. |

## Build

The project currently uses Android Gradle Plugin 7.4.2 and Java 11. GitHub Actions builds both release modules on pushes, pull requests, and manual runs.

To build locally, install JDK 11 and Gradle 7.6.4, then run:

```bash
gradle --no-daemon assembleRelease
```

The APKs are generated under each module's `build/outputs/apk/release/` directory. CI uploads them as a workflow artifact.

## Install

For a local test, install the generated APK on the Android device where Aniyomi is installed. The extension package must be compatible with the Aniyomi version in use. A successful Gradle build alone does not mean the source's website parsing or video playback has been verified.

## Repository status

The project is being brought up to a reproducible build first. The HiAnime source still needs its current `hianime.ms` page, episode, server, and stream flows implemented and tested. The AnimePahe module does not yet contain a source class. No public extension index or signed release is published yet.

## Disclaimer

This project is independent and is not affiliated with Aniyomi or the websites it connects to. Use only content you are authorized to access.
