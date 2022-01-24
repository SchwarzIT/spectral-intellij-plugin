package com.schwarzit.spectralIntellijPlugin.settings;

import com.schwarzit.spectralIntellijPlugin.exceptions.ProjectSettingsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectSettingsConfigurableTest {

    private ProjectSettingsConfigurable projectSettingsConfigurable;
    private ProjectSettingsState projectSettingsStateMock;

    @BeforeEach
    void setUp() throws ProjectSettingsException {
        try (MockedConstruction<BaseSettingsComponent> ignored = mockConstruction(BaseSettingsComponent.class)) {
            projectSettingsStateMock = mock(ProjectSettingsState.class);

            ProjectSettingsState.instance = projectSettingsStateMock;
            projectSettingsConfigurable = new ProjectSettingsConfigurable();
        }
    }

    @Test
    void getDisplayName() {
        assertEquals("Spectral", projectSettingsConfigurable.getDisplayName());
    }

    @Test
    void createComponent() {
        assertEquals(projectSettingsConfigurable.uiComponent.getPanel(), projectSettingsConfigurable.createComponent());
    }

    @Test
    void getPreferredFocusedComponent() {
        assertEquals(projectSettingsConfigurable.uiComponent.getPreferredFocusedComponent(), projectSettingsConfigurable.getPreferredFocusedComponent());
    }

    @Test
    void isModified() {
        when(projectSettingsStateMock.equals(isA(BaseSettingsComponent.class))).thenReturn(false);
        assertTrue(projectSettingsConfigurable.isModified());
    }

    @Test
    void disposeUIResources() {
        projectSettingsConfigurable.disposeUIResources();
        assertNull(projectSettingsConfigurable.uiComponent);
    }
}