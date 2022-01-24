package com.schwarzit.spectralIntellijPlugin.models;

import com.schwarzit.spectralIntellijPlugin.config.Config;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PluginSettingsTest {
    @Test
    void testSetRuleset() {
        String ruleset = Config.Instance.DEFAULT_RULESET_URL();
        String includedFiles = Config.Instance.DEFAULT_INCLUDED_FILES_PATTERN();

        PluginSettings pluginSettings = new PluginSettings(ruleset, includedFiles);

        pluginSettings.setRuleset("Test");
        assertEquals("Test", pluginSettings.getRuleset());
        pluginSettings.setRuleset("");
        assertEquals(ruleset, pluginSettings.getRuleset());
    }

    @Test
    void testSetIncludedFiles() {
        String ruleset = Config.Instance.DEFAULT_RULESET_URL();
        String includedFiles = Config.Instance.DEFAULT_INCLUDED_FILES_PATTERN();

        PluginSettings pluginSettings = new PluginSettings(ruleset, includedFiles);

        pluginSettings.setIncludedFiles("Test");
        assertEquals("Test", pluginSettings.getIncludedFiles());
        pluginSettings.setIncludedFiles("");
        assertEquals(includedFiles, pluginSettings.getIncludedFiles());
    }
}