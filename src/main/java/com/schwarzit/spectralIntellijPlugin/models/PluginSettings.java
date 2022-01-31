package com.schwarzit.spectralIntellijPlugin.models;

import com.intellij.notification.NotificationType;
import com.schwarzit.spectralIntellijPlugin.StorageManager;
import com.schwarzit.spectralIntellijPlugin.config.Config;
import com.schwarzit.spectralIntellijPlugin.exceptions.SpectralException;
import com.schwarzit.spectralIntellijPlugin.settings.BaseSettingsComponent;
import com.schwarzit.spectralIntellijPlugin.util.NotificationHandler;

import java.util.Objects;

public class PluginSettings {
    private String ruleset;
    private String includedFiles;

    @SuppressWarnings("unused") // NoArgs constructor is needed for serialization
    public PluginSettings() {
    }

    public PluginSettings(String ruleset, String includedFiles) {
        this.ruleset = ruleset;
        this.includedFiles = includedFiles;
    }

    public String getRuleset() {
        return ruleset;
    }

    public void setRuleset(String ruleset) {
        if (ruleset.isBlank()) {
            try {
                this.ruleset = StorageManager.getInstance().getStoragePath().resolve(Config.Instance.DEFAULT_RULESET_NAME()).toAbsolutePath().toString();
            } catch (SpectralException e) {
                NotificationHandler.getInstance().showNotification("Failed to resolve path of default Ruleset", Config.Instance.DEFAULT_RULESET_NAME(), NotificationType.ERROR);
            }
        } else {
            this.ruleset = ruleset;
        }
    }

    public String getIncludedFiles() {
        return includedFiles;
    }

    public void setIncludedFiles(String includedFiles) {
        if (includedFiles.isBlank()) {
            this.includedFiles = Config.Instance.DEFAULT_INCLUDED_FILES_PATTERN();
        } else {
            this.includedFiles = includedFiles;
        }
    }

    public boolean equals(BaseSettingsComponent ui) {
        return Objects.equals(this.ruleset, ui.getRulesetPath()) && Objects.equals(this.includedFiles, ui.getIncludedFiles());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PluginSettings settings = (PluginSettings) o;
        return Objects.equals(ruleset, settings.ruleset) && Objects.equals(includedFiles, settings.includedFiles);
    }
}
