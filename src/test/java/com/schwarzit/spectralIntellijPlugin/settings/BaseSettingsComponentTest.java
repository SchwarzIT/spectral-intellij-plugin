package com.schwarzit.spectralIntellijPlugin.settings;

import com.intellij.openapi.project.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class BaseSettingsComponentTest {
    private BaseSettingsComponent baseSettingsComponent;

    @BeforeEach
    void setUp() {
        baseSettingsComponent = new BaseSettingsComponent();
    }

    @Test
    void getPanel() {
        assertEquals(baseSettingsComponent.mainPanel, baseSettingsComponent.getPanel());
    }

    @Test
    void getPreferredFocusedComponent() {
        assertEquals(baseSettingsComponent.rulesetPath, baseSettingsComponent.getPreferredFocusedComponent());
    }

    @Test
    void setProject() {
        Project project = mock(Project.class);
        BaseSettingsComponent.setProject(project);
        assertEquals(project, BaseSettingsComponent.project);
    }

    @Test
    void getRulesetPath() {
        assertEquals(baseSettingsComponent.rulesetPath.getText(), baseSettingsComponent.getRulesetPath());
    }

    @Test
    void setRulesetPath() {
        baseSettingsComponent.setRulesetPath("TestRulesetPath");
        assertEquals("TestRulesetPath", baseSettingsComponent.getRulesetPath());
    }

    @Test
    void getIncludedFiles() {
        assertEquals(baseSettingsComponent.includedFiles.getText(), baseSettingsComponent.getIncludedFiles());
    }

    @Test
    void setIncludedFiles() {
        baseSettingsComponent.setIncludedFiles("TestIncludedFiles");
        assertEquals("TestIncludedFiles", baseSettingsComponent.getIncludedFiles());
    }
}