package com.schwarzit.spectralIntellijPlugin.models;

import com.schwarzit.spectralIntellijPlugin.StorageManager;
import com.schwarzit.spectralIntellijPlugin.config.Config;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PluginSettingsTest {
    @Test
    void testSetRuleset() {
        String ruleset = "/" + Config.Instance.DEFAULT_RULESET_NAME();
        String includedFiles = Config.Instance.DEFAULT_INCLUDED_FILES_PATTERN();

        PluginSettings pluginSettings = new PluginSettings(ruleset, includedFiles);

        try (MockedStatic<StorageManager> storageManagerMockedStatic = mockStatic(StorageManager.class)) {
            StorageManager storageManager = mock(StorageManager.class);
            when(storageManager.getStoragePath()).thenReturn(Path.of("/"));
            storageManagerMockedStatic.when(StorageManager::getInstance).thenReturn(storageManager);

            pluginSettings.setRuleset("Test");
            assertEquals("Test", pluginSettings.getRuleset());
            pluginSettings.setRuleset("");
            assertEquals(ruleset, pluginSettings.getRuleset());
        }

    }

    @Test
    void testSetIncludedFiles() {
        String ruleset = Config.Instance.DEFAULT_RULESET_NAME();
        String includedFiles = Config.Instance.DEFAULT_INCLUDED_FILES_PATTERN();

        PluginSettings pluginSettings = new PluginSettings(ruleset, includedFiles);

        pluginSettings.setIncludedFiles("Test");
        assertEquals("Test", pluginSettings.getIncludedFiles());
        pluginSettings.setIncludedFiles("");
        assertEquals(includedFiles, pluginSettings.getIncludedFiles());
    }
}