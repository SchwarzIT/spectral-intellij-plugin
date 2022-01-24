package com.schwarzit.spectralIntellijPlugin.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.util.FileContentUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public abstract class BaseSettingsConfigurable implements Configurable {
    protected BaseSettingsComponent uiComponent;
    protected final BaseSettingsState settings;

    public BaseSettingsConfigurable(BaseSettingsComponent uiComponent, BaseSettingsState settings) {
        this.uiComponent = uiComponent;
        this.settings = settings;
    }

    @Override
    @Nls(capitalization = Nls.Capitalization.Title)
    public abstract String getDisplayName();

    @Override
    public @Nullable JComponent createComponent() {
        return uiComponent.getPanel();
    }

    @Override
    public @Nullable JComponent getPreferredFocusedComponent() {
        return uiComponent.getPreferredFocusedComponent();
    }

    @Override
    public boolean isModified() {
        return !settings.equals(uiComponent);
    }

    @Override
    public void reset() {
        uiComponent.setRulesetPath(settings.getRuleset());
        uiComponent.setIncludedFiles(settings.getIncludedFiles());
    }

    @Override
    public void apply() {
        settings.setRuleset(uiComponent.getRulesetPath());
        settings.setIncludedFiles(uiComponent.getIncludedFiles());
        FileContentUtil.reparseOpenedFiles();
    }

    @Override
    public void disposeUIResources() {
        uiComponent = null;
    }
}
