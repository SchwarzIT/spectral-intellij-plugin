package com.schwarzit.spectralIntellijPlugin.config;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings("SameReturnValue")
public enum Config {
    Instance;

    public @NotNull String DEFAULT_RULESET_URL() {
        return "https://raw.githubusercontent.com/openapi-contrib/style-guides/master/openapi.yml";
    }

    public @NotNull String DEFAULT_RULESET_NAME() {
        return "default-ruleset.yml";
    }

    public @NotNull String DEFAULT_INCLUDED_FILES_PATTERN() {
        return "**.json";
    }
}
