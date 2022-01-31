package com.schwarzit.spectralIntellijPlugin;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.openapi.extensions.PluginId;
import com.schwarzit.spectralIntellijPlugin.exceptions.SpectralException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.mock;

class StorageManagerTest {

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        Field instance = StorageManager.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Linux", "Unix", "Windows", "Mac OS X"})
    void testInstanceCreation(String os) throws SpectralException {
        try (MockedStatic<PluginManagerCore> pluginManagerCoreMockedStatic = Mockito.mockStatic(PluginManagerCore.class)) {
            IdeaPluginDescriptor pluginDescriptorMock = mock(IdeaPluginDescriptor.class);
            Mockito.when(pluginDescriptorMock.getPluginPath()).thenReturn(Path.of("/"));
            pluginManagerCoreMockedStatic.when(() -> PluginManagerCore.getPlugin(isA(PluginId.class))).thenReturn(pluginDescriptorMock);

            System.setProperty("os.name", os);

            StorageManager storageManager = StorageManager.getInstance();
            Assertions.assertNotNull(storageManager);
        }
    }

    @Test
    void testInstanceCreationUnsupportedOS() {
        try (MockedStatic<PluginManagerCore> pluginManagerCoreMockedStatic = Mockito.mockStatic(PluginManagerCore.class)) {
            IdeaPluginDescriptor pluginDescriptorMock = mock(IdeaPluginDescriptor.class);
            Mockito.when(pluginDescriptorMock.getPluginPath()).thenReturn(Path.of("/"));
            pluginManagerCoreMockedStatic.when(() -> PluginManagerCore.getPlugin(isA(PluginId.class))).thenReturn(pluginDescriptorMock);

            System.setProperty("os.name", "Mark OS");

            Exception exception = assertThrows(SpectralException.class, StorageManager::getInstance);
            Assertions.assertTrue(exception.getMessage().contains("Mark OS"));
        }
    }

    @Test
    void testInstanceCreationUnresolvedStoragePath() {
        try (MockedStatic<PluginManagerCore> pluginManagerCoreMockedStatic = Mockito.mockStatic(PluginManagerCore.class)) {
            IdeaPluginDescriptor pluginDescriptorMock = mock(IdeaPluginDescriptor.class);
            Mockito.when(pluginDescriptorMock.getPluginPath()).thenReturn(Path.of("/"));
            pluginManagerCoreMockedStatic.when(() -> PluginManagerCore.getPlugin(isA(PluginId.class))).thenReturn(null);

            Exception exception = assertThrows(SpectralException.class, StorageManager::getInstance);
            Assertions.assertTrue(exception.getMessage().contains("Failed to get the installation path of the plugin"));
        }
    }
}