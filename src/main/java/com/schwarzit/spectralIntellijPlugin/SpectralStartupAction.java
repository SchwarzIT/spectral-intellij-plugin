package com.schwarzit.spectralIntellijPlugin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.schwarzit.spectralIntellijPlugin.config.Config;
import com.schwarzit.spectralIntellijPlugin.exceptions.SpectralException;
import com.schwarzit.spectralIntellijPlugin.settings.BaseSettingsComponent;
import com.schwarzit.spectralIntellijPlugin.settings.ProjectSettingsState;
import com.schwarzit.spectralIntellijPlugin.util.NotificationHandler;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;

public class SpectralStartupAction implements StartupActivity {
    @Override
    public void runActivity(@NotNull Project project) {
        ProjectSettingsState.initialize(project);
        BaseSettingsComponent.setProject(project);
        NotificationHandler.getInstance().setProject(project);

        try {
            StorageManager storageManager = StorageManager.getInstance();
            storageManager.downloadRuleset(new URL(Config.Instance.DEFAULT_RULESET_URL()), storageManager.getDefaultRulesetPath());
        } catch (MalformedURLException e) {
            throw new RuntimeException("The Url of the default ruleset is malformed: " + Config.Instance.DEFAULT_RULESET_URL(), e);
        } catch (SpectralException e) {
            throw new RuntimeException(e);
        }
    }
}
