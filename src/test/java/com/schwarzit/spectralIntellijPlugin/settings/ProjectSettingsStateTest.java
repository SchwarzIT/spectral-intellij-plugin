package com.schwarzit.spectralIntellijPlugin.settings;

import com.schwarzit.spectralIntellijPlugin.config.Config;
import com.schwarzit.spectralIntellijPlugin.models.PluginSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProjectSettingsStateTest {
    private BaseSettingsState settings;

    @BeforeEach
    void setUp() {
        settings = new ProjectSettingsState();
    }

    @Test
    void testProjectSettingsDefault() {
        PluginSettings defaultSettings = new PluginSettings(Config.Instance.DEFAULT_RULESET_NAME(), Config.Instance.DEFAULT_INCLUDED_FILES_PATTERN());
        assertEquals(defaultSettings, settings.getState());
    }

    @Test
    void testLoadState() {
        PluginSettings loadedState = new PluginSettings("TestRuleset", "TestIncludedFiles");
        settings.loadState(loadedState);
        assertEquals(loadedState, settings.getState());
    }
}