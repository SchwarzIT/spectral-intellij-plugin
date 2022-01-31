package com.schwarzit.spectralIntellijPlugin.settings;

import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.schwarzit.spectralIntellijPlugin.config.Config;
import com.schwarzit.spectralIntellijPlugin.exceptions.ProjectSettingsException;
import com.schwarzit.spectralIntellijPlugin.models.PluginSettings;

@State(
        name = "com.schwarzit.spectralIntellijPlugin.settings.ProjectSettingsState",
        storages = @Storage("SpectralSettings.xml")
)
public class ProjectSettingsState extends BaseSettingsState {
    protected static ProjectSettingsState instance;

    public ProjectSettingsState() {
        super(new PluginSettings(Config.Instance.DEFAULT_RULESET_NAME(), Config.Instance.DEFAULT_INCLUDED_FILES_PATTERN()));
    }

    public static void initialize(Project project) {
        instance = project.getService(ProjectSettingsState.class);
    }

    public static ProjectSettingsState getInstance() throws ProjectSettingsException {
        if (instance == null) {
            throw new ProjectSettingsException("Instance of ProjectSettingsState was not initialized before first usage");
        }
        return instance;
    }
}
