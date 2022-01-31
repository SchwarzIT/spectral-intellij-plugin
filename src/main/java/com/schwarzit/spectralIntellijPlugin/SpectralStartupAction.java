package com.schwarzit.spectralIntellijPlugin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.schwarzit.spectralIntellijPlugin.exceptions.SpectralException;
import com.schwarzit.spectralIntellijPlugin.settings.BaseSettingsComponent;
import com.schwarzit.spectralIntellijPlugin.settings.ProjectSettingsState;
import com.schwarzit.spectralIntellijPlugin.util.NotificationHandler;
import org.jetbrains.annotations.NotNull;

public class SpectralStartupAction implements StartupActivity {
    @Override
    public void runActivity(@NotNull Project project) {
        ProjectSettingsState.initialize(project);
        BaseSettingsComponent.setProject(project);
        NotificationHandler.getInstance().setProject(project);

        try {
            StorageManager storageManager = StorageManager.getInstance();
            storageManager.installSpectralBinary();
            storageManager.installDefaultRuleset();
        } catch (SpectralException e) {
            throw new RuntimeException(e);
        }
    }
}
