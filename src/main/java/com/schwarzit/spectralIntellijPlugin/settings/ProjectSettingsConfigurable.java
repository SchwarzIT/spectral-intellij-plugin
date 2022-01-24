package com.schwarzit.spectralIntellijPlugin.settings;

import com.schwarzit.spectralIntellijPlugin.exceptions.ProjectSettingsException;
import org.jetbrains.annotations.Nls;

public class ProjectSettingsConfigurable extends BaseSettingsConfigurable {
    public ProjectSettingsConfigurable() throws ProjectSettingsException {
        super(new BaseSettingsComponent(), ProjectSettingsState.getInstance());
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Spectral";
    }
}
