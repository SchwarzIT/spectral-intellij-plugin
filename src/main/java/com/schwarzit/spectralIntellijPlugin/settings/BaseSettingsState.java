package com.schwarzit.spectralIntellijPlugin.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.schwarzit.spectralIntellijPlugin.models.PluginSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BaseSettingsState implements PersistentStateComponent<PluginSettings> {
    protected final PluginSettings settings;

    public BaseSettingsState(PluginSettings settings) {
        this.settings = settings;
    }

    @Nullable
    @Override
    public PluginSettings getState() {
        return this.settings;
    }

    @Override
    public void loadState(@NotNull PluginSettings state) {
        XmlSerializerUtil.copyBean(state, this.settings);
    }

    public String getRuleset() {
        return this.settings.getRuleset();
    }

    public void setRuleset(String ruleset) {
        this.settings.setRuleset(ruleset);
    }

    public String getIncludedFiles() {
        return this.settings.getIncludedFiles();
    }

    public void setIncludedFiles(String includedFiles) {
        this.settings.setIncludedFiles(includedFiles);
    }

    public boolean equals(BaseSettingsComponent ui) {
        return this.settings.equals(ui);
    }
}
