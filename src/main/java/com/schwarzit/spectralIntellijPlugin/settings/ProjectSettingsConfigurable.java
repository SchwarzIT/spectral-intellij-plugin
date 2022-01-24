package com.schwarzit.spectralIntellijPlugin.settings;

import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;

public class ProjectSettingsConfigurable extends BaseSettingsConfigurable {
    public ProjectSettingsConfigurable(Project project) {
        super(new BaseSettingsComponent(), ProjectSettingsState.getInstance(project));
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Spectral";
    }
}
