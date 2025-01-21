<!-- @formatter:off -->
<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Changelog

## [Unreleased]

### Fixed

- Make com.intellij.modules.json an explicit dependency to resolve plugin validation issue

## [3.0.1] - 2023-10-20

### Fixed

- Fix pattern detection in relative path condition

## [3.0.0] - 2023-10-04

### Added

- Improve check for files to lint on non unix operating systems

### Changed

- [BREAKING] Use [ant pattern matching](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/util/AntPathMatcher.html) to check for included files. This might be a breaking change for some people, please check your included files in the settings if you encounter any issues. E.g. the default inclusion pattern `**openapi.json` would now no longer match the path `openapi/v1-openapi.json`, please use `**/*openapi.json` instead.

## [2.1.3] - 2023-09-01

### Changed

- Update dependencies

## [2.1.2] - 2023-08-28

### Changed

- Refactor error handling for failure during file inclusion check to be more verbose

## [2.1.1] - 2023-08-02

### Changed

- Delete unused constructor from SpectralException
- Turn SpectralRunner into a light service
- Turn ProjectSettingsState into a light service
- Reformat code

## [2.1.0] - 2023-08-02

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

[Unreleased]: https://github.com/SchwarzIT/spectral-intellij-plugin/compare/v3.0.1...HEAD
[3.0.1]: https://github.com/SchwarzIT/spectral-intellij-plugin/compare/v3.0.0...v3.0.1
[3.0.0]: https://github.com/SchwarzIT/spectral-intellij-plugin/compare/v2.1.3...v3.0.0
[2.1.3]: https://github.com/SchwarzIT/spectral-intellij-plugin/compare/v2.1.2...v2.1.3
[2.1.2]: https://github.com/SchwarzIT/spectral-intellij-plugin/compare/v2.1.1...v2.1.2
[2.1.1]: https://github.com/SchwarzIT/spectral-intellij-plugin/compare/v2.1.0...v2.1.1
[2.1.0]: https://github.com/SchwarzIT/spectral-intellij-plugin/compare/v2.0.0...v2.1.0
[2.0.0]: https://github.com/SchwarzIT/spectral-intellij-plugin/compare/v2.0.0-alpha.3...v2.0.0
[2.0.0-alpha.1]: https://github.com/SchwarzIT/spectral-intellij-plugin/commits/v2.0.0-alpha.1
[2.0.0-alpha.2]: https://github.com/SchwarzIT/spectral-intellij-plugin/compare/v2.0.0-alpha.1...v2.0.0-alpha.2
[2.0.0-alpha.3]: https://github.com/SchwarzIT/spectral-intellij-plugin/compare/v2.0.0-alpha.2...v2.0.0-alpha.3
