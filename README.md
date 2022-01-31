# Spectral IntelliJ Plugin

This plugin is a wrapper for the tool [Spectral](https://github.com/stoplightio/spectral), a linter for OpenApi schemas.
It supports all Jetbrains IDEs starting at version 2020.3.

## Features

- Automatic linting of your OpenApi specifications and highlighting in your editor
- Specify your own [ruleset](https://meta.stoplight.io/docs/spectral/ZG9jOjYyMDc0NA-rulesets) in the plugins settings,
  under Preferences -> Tools -> Spectral -> Ruleset. There you can select a file on your local machine or just paste the
  URL of a ruleset available on the internet e.g.:
  [Schwarz IT API linting rules](https://github.com/SchwarzIT/api-linter-rules)

  The default ruleset comes bundled with the plugin and uses Spectrals
  recommended [OAS](https://meta.stoplight.io/docs/spectral/ZG9jOjExNw-open-api-rules) ruleset.
- Select the files that will be linted. By default, every JSON file will be linted by the plugin when it's opened. You
  can adjust this in the Settings under Preferences -> Tools -> Spectral -> Included files. Default is the glob
  pattern `**.json`, you could for example change it to `**/openapi/*.json`

## Installation

### From the Jetbrains Marketplace

Coming soon...

### Building from source

1. Checkout this repository
2. Run ./gradlew buildPlugin
3. Install the generated archive under build/distributions/spectral-intellij-plugin*.zip in your IDE (
   See [Install plugin from disk](https://www.jetbrains.com/help/idea/managing-plugins.html#install_plugin_from_disk))
