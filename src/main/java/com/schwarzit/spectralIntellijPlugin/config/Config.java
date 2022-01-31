package com.schwarzit.spectralIntellijPlugin.config;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings("SameReturnValue")
public enum Config {
    Instance;

    public @NotNull String DEFAULT_RULESET_NAME() {
        return "default-ruleset.yml";
    }

    public @NotNull String DEFAULT_INCLUDED_FILES_PATTERN() {
        return "**.json";
    }

    public String ERROR_REPORTING_URL() {
        return "https://github.com/SchwarzIT/spectral-intellij-plugin/issues/new";
    }
}
