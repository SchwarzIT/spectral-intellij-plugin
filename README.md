# Spectral IntelliJ Plugin

[![SIT](https://img.shields.io/badge/SIT-awesome-blueviolet.svg)](https://jobs.schwarz)

This plugin is a wrapper for the tool [Spectral](https://github.com/stoplightio/spectral), a linter for OpenApi schemas.
It supports all Jetbrains IDEs starting at version 2020.3.

## Features

### Automatic Linting
Automatic linting of your OpenApi specifications and highlighting in your editor

### Customizable Ruleset
Specify your own [ruleset](https://meta.stoplight.io/docs/spectral/ZG9jOjYyMDc0NA-rulesets) in the plugins settings,
  under `Preferences -> Tools -> Spectral -> Ruleset`. There you can select a file on your local machine or just paste the
  URL of a ruleset available on the internet e.g.:
  [Schwarz IT API linting rules](https://github.com/SchwarzIT/api-linter-rules)

  The default ruleset comes bundled with the plugin and uses Spectrals
  recommended [OAS](https://meta.stoplight.io/docs/spectral/ZG9jOjExNw-open-api-rules) ruleset.
### Customizable File Matching pattern
The customization of file matching is possible under `Preferences -> Tools -> Spectral -> Included files`. By 
default, every JSON file will be linted with default pattern `**.json` by the plugin, when json file is opened. You 
can adjust this to `**/openapi/*.json`(e.g. matches openapi.json), so that some other json files, such as `composer.
json`, `package.json`, will
not be included and linted automatically.

## Installation

### From the Jetbrains Marketplace

The latest version of the plugin is available on the [Jetbrains marketplace](https://plugins.jetbrains.com/plugin/18520-spectral). To install it you can follow these
steps:

1. Open your Jetbrains IDE
2. Go to Preferences -> Plugins and search for "Spectral"
3. Click install on the first result

### Building from source

1. Checkout this repository
2. Run ./gradlew buildPlugin
3. Install the generated archive under build/distributions/spectral-intellij-plugin*.zip in your IDE (
   See [Install plugin from disk](https://www.jetbrains.com/help/idea/managing-plugins.html#install_plugin_from_disk))
