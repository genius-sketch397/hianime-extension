# HiAnime & AnimePahe Extensions for Aniyomi

Custom Aniyomi extensions for HiAnime and AnimePahe sources. These extensions allow you to stream anime from these sources directly through Aniyomi.

## Features

- **HiAnime Extension** - Stream anime from hianime.ms
- **AnimePahe Extension** - Stream anime from animepahe.pw
- Easy domain updates when sources change
- Regular maintenance and updates

## Installation

1. Download the latest APK from releases
2. Install on your device
3. Open Aniyomi and enable the extensions in Settings > Extensions

## Building from Source

### Prerequisites
- Android Studio
- JDK 11 or higher
- Gradle 7.0+

### Build Instructions

```bash
git clone https://github.com/genius-sketch397/hianime-extension.git
cd hianime-extension
./gradlew assembleDebug
```

The APK will be generated in `build/outputs/apk/debug/`

## Project Structure

```
hianime-extension/
├── hianime/
│   ├── src/main/kotlin/eu/kanade/tachiyomi/animeextension/english/hianime/
│   ├── build.gradle
│   └── AndroidManifest.xml
├── animepahe/
│   ├── src/main/kotlin/eu/kanade/tachiyomi/animeextension/english/animepahe/
│   ├── build.gradle
│   └── AndroidManifest.xml
├── build.gradle
├── settings.gradle
└── README.md
```

## Updating Domains

When domain changes occur:
1. Update the base URL in the respective extension class
2. Rebuild the extension
3. Release a new version

### Current URLs
- **HiAnime**: https://hianime.ms
- **AnimePahe**: https://animepahe.pw

## Support

For issues or feature requests, please create an issue on this repository.

## Disclaimer

This project is for educational purposes only. Use at your own risk.
