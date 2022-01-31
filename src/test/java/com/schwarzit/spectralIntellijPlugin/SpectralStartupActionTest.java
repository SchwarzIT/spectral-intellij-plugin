package com.schwarzit.spectralIntellijPlugin;

import com.intellij.openapi.project.Project;
import com.schwarzit.spectralIntellijPlugin.exceptions.SpectralException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

class SpectralStartupActionTest {

    private SpectralStartupAction spectralStartupAction;

    @BeforeEach
    void setUp() {
        spectralStartupAction = new SpectralStartupAction();
    }

    @Test
    void runActivity() throws SpectralException {
        try (MockedStatic<StorageManager> storageManagerMockedStatic = Mockito.mockStatic(StorageManager.class)) {
            StorageManager storageManagerMock = mock(StorageManager.class);
            storageManagerMockedStatic.when(StorageManager::getInstance).thenReturn(storageManagerMock);
            doNothing().when(storageManagerMock).installSpectralBinary();
            doNothing().when(storageManagerMock).installDefaultRuleset();

            spectralStartupAction.runActivity(mock(Project.class));
        }
    }

    @Test
    void runActivityWithSpectralException() throws SpectralException {
        try (MockedStatic<StorageManager> storageManagerMockedStatic = Mockito.mockStatic(StorageManager.class)) {
            StorageManager storageManagerMock = mock(StorageManager.class);
            storageManagerMockedStatic.when(StorageManager::getInstance).thenThrow(new SpectralException("Test"));
            doNothing().when(storageManagerMock).installSpectralBinary();
            doNothing().when(storageManagerMock).installDefaultRuleset();

            assertThrows(RuntimeException.class, () -> spectralStartupAction.runActivity(mock(Project.class)));
        }
    }
}