package com.schwarzit.spectralIntellijPlugin;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.extensions.PluginId;
import com.schwarzit.spectralIntellijPlugin.exceptions.DownloadFailedException;
import com.schwarzit.spectralIntellijPlugin.exceptions.SpectralException;
import com.schwarzit.spectralIntellijPlugin.util.FileDownloader;
import com.schwarzit.spectralIntellijPlugin.util.NotificationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

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

    @Test
    void testDownloadRuleset() throws SpectralException, MalformedURLException {
        try (MockedStatic<PluginManagerCore> pluginManagerCoreMockedStatic = Mockito.mockStatic(PluginManagerCore.class)) {
            IdeaPluginDescriptor pluginDescriptorMock = mock(IdeaPluginDescriptor.class);
            when(pluginDescriptorMock.getPluginPath()).thenReturn(Path.of("/"));
            pluginManagerCoreMockedStatic.when(() -> PluginManagerCore.getPlugin(isA(PluginId.class))).thenReturn(pluginDescriptorMock);

            try (MockedStatic<FileDownloader> fileDownloaderMockedStatic = Mockito.mockStatic(FileDownloader.class)) {
                URL url = new URL("https://www.google.com");
                NotificationHandler notificationHandlerMock = mock(NotificationHandler.class);
                fileDownloaderMockedStatic.when(() -> FileDownloader.downloadFile(isA(URL.class), isA(String.class))).thenThrow(new DownloadFailedException(url));
                doNothing().when(notificationHandlerMock).showNotification(any(), any(), any());

                StorageManager instance = new StorageManager(notificationHandlerMock);
                assertThrows(RuntimeException.class, () -> instance.downloadRuleset(url, Path.of("/destination")));

                verify(notificationHandlerMock).showNotification(eq("Ruleset download failed"), contains(url.toString()), eq(NotificationType.ERROR));
            }
        }
    }
}