<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Changelog

## [Unreleased]

### Changed

- Complete rewrite of the plugin in Kotlin

## [1.0.7] - 2022-05-19

### Added

- Added logging for debugging. Now you can open the IDE logs under Help -> Show log in Finder/Explorer

## [1.0.6] - 2022-05-16

### Changed

- Updated spectral to v6.2.1

## [1.0.5] - 2022-04-04

### Added

- Added hot reloading for local rule-sets

## [1.0.4] - 2022-04-04

### Added

- Added support for linting OpenAPI specs referencing other files e.g. DTOs using a relative path

## [1.0.3] - 2022-02-17

### Added

- Added support for specifying multiple glob patterns inside included files by splitting them with a semicolon e.g.: "*
  *.json;**.yml"
- Improved debugging by writing the error message coming from spectral to the event log

## [1.0.2] - 2022-02-14

### Added

- Added support for linting yaml files

## [1.0.1] - 2022-02-01

### Changed

- Changed Vendor to not contains spaces, so it can be used as the uid of an Jetbrains organization
- Added the link to the open source repository of the plugin

## [1.0.0] - 2022-01-31

### Added

- Initial release
