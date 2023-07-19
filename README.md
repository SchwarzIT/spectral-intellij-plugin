# jetbrains-plugin-template

![Build](https://github.com/markbrockhoff/jetbrains-plugin-template/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/com.schwarzit.spectral-intellij-plugin.svg)](https://plugins.jetbrains.com/plugin/com.schwarzit.spectral-intellij-plugin)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/com.schwarzit.spectral-intellij-plugin.svg)](https://plugins.jetbrains.com/plugin/com.schwarzit.spectral-intellij-plugin)

<!-- Plugin description -->
This plugin is a wrapper for the tool <a href="https://github.com/stoplightio/spectral">Spectral</a>, a linter for
OpenApi schemas.

## Installation

- Using IDE built-in plugin system:

  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "Spectral"</kbd> >
  <kbd>Install Plugin</kbd>

- Manually:

  Download the [latest release](https://github.com/markbrockhoff/jetbrains-plugin-template/releases/latest) and install
  it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

Since version 2 of this plugin it is required to have the spectral CLI installed on your system.
If you don't have it installed yet make sure to follow this
guide: [Installing Spectral](https://docs.stoplight.io/docs/spectral/b8391e051b7d8-installation)

_Note: The CLI needs to be in your path and executable from the command line._

## Features

### Automatic linting

Automatic linting of your OpenApi specifications and highlighting in your editor

### Customizable Ruleset

Specify your own [ruleset](https://meta.stoplight.io/docs/spectral/ZG9jOjYyMDc0NA-rulesets) in the plugins
settings, under Preferences -> Tools -> Spectral -> Ruleset.
There you can specify a file on your local machine or just paste the URL of a ruleset available on the internet
e.g.: [Schwarz IT API linting rules](https://github.com/SchwarzIT/api-linter-rules).

Examples:

- Link to a hosted ruleset: https://raw.githubusercontent.com/SchwarzIT/api-linter-rules/main/spectral-api.yml
- Local ruleset inside the project: ".spectral.json"

### Customizable file matching

Select the files that will be linted. By default, every JSON or YAML file will be linted by the plugin when it's
opened.
You can adjust this in the Settings under Preferences -> Tools -> Spectral -> Included files. All paths are relative
to the projects working directory.

Examples:

- openapi.json: Matches the file called "openapi.json" inside the root directory of the project
- components/**.json: Matches all files inside the directory "components" that end with ".json"

<!-- Plugin description end -->

---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template

[docs:plugin-description]: https://plugins.jetbrains.com/docs/intellij/plugin-user-experience.html#plugin-description-and-presentation