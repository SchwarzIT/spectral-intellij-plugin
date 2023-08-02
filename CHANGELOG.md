<!-- @formatter:off -->
<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Changelog

## [Unreleased]


### Changed
- Make compatible with IntelliJ IDEA 2023.2
- Add support for absolute paths in file-matcher
- Fixes & improvements to `README.md`
- Add name of file being analysed to transient status message
- Log an error if `spectral` execution fails due to misconfiguration. This is not ideal but at least offers some user feedback.

## [2.0.0] - 2023-07-24

### Changed
- Improve error handling in case of an invalid openapi spec
- Update dependency `org.jetbrains.changelog` from v2.0.0 to v2.1.2
- Update dependency `org.jetbrains.changelog` from v0.6.0 to v0.7.2
- Complete rewrite of the plugin in Kotlin
- BREAKING: Spectral no longer comes bundled with the plugin and needs to be installed additionally now
- BREAKING: Settings now only apply on a project level

## [2.0.0-alpha.3] - 2023-07-21

### Changed
- Improve error handling in case of an invalid openapi spec
- Update dependency `org.jetbrains.changelog` from v2.0.0 to v2.1.2
- Update dependency `org.jetbrains.changelog` from v0.6.0 to v0.7.2
- Complete rewrite of the plugin in Kotlin
- BREAKING: Spectral no longer comes bundled with the plugin and needs to be installed additionally now
- BREAKING: Settings now only apply on a project level

## [2.0.0-alpha.2] - 2023-07-21

### Changed
- Update dependency `org.jetbrains.changelog` from v2.0.0 to v2.1.2
- Update dependency `org.jetbrains.changelog` from v0.6.0 to v0.7.2
- Complete rewrite of the plugin in Kotlin
- BREAKING: Spectral no longer comes bundled with the plugin and needs to be installed additionally now
- BREAKING: Settings now only apply on a project level

## [2.0.0-alpha.1] - 2023-07-20

### Changed
- Complete rewrite of the plugin in Kotlin
- BREAKING: Spectral no longer comes bundled with the plugin and needs to be installed additionally now
- BREAKING: Settings now only apply on a project level

[Unreleased]: https://github.com/SchwarzIT/spectral-intellij-plugin/compare/v2.0.0...HEAD
[2.0.0]: https://github.com/SchwarzIT/spectral-intellij-plugin/compare/v2.0.0-alpha.3...v2.0.0
[2.0.0-alpha.1]: https://github.com/SchwarzIT/spectral-intellij-plugin/commits/v2.0.0-alpha.1
[2.0.0-alpha.2]: https://github.com/SchwarzIT/spectral-intellij-plugin/compare/v2.0.0-alpha.1...v2.0.0-alpha.2
[2.0.0-alpha.3]: https://github.com/SchwarzIT/spectral-intellij-plugin/compare/v2.0.0-alpha.2...v2.0.0-alpha.3
