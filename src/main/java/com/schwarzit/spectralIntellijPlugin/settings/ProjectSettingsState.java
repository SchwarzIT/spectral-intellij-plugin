package com.schwarzit.spectralIntellijPlugin.settings;

import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.schwarzit.spectralIntellijPlugin.config.Config;
import com.schwarzit.spectralIntellijPlugin.models.PluginSettings;

@State(
        name = "com.schwarzit.spectralIntellijPlugin.settings.ProjectSettingsState",
        storages = @Storage("SpectralSettings.xml")
)
public class ProjectSettingsState extends BaseSettingsState {
    public ProjectSettingsState() {
        super(new PluginSettings(Config.Instance.DEFAULT_RULESET_URL(), Config.Instance.DEFAULT_INCLUDED_FILES_PATTERN()));
    }

    public static ProjectSettingsState getInstance(Project project) {
        return project.getService(ProjectSettingsState.class);
    }
}
