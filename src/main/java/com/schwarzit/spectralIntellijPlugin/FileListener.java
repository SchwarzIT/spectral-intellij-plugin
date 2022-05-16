package com.schwarzit.spectralIntellijPlugin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.AsyncFileListener;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.newvfs.events.VFileEvent;
import com.schwarzit.spectralIntellijPlugin.exceptions.ProjectSettingsException;
import com.schwarzit.spectralIntellijPlugin.settings.ProjectSettingsState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.List;

public class FileListener implements AsyncFileListener {
    @Override
    public @Nullable ChangeApplier prepareChange(@NotNull List<? extends VFileEvent> events) {
        try {
            ProjectSettingsState storageManager = ProjectSettingsState.getInstance();
            // Hot reloading of changes to the ruleset only works for local rule sets
            if (storageManager.getRuleset().startsWith("http")) return null;

            ProjectManager projectManager = ProjectManager.getInstanceIfCreated();
            if (projectManager == null) return null;

            Project project = projectManager.getDefaultProject();
            VirtualFile[] contentRoots = ProjectRootManager.getInstance(project).getContentRoots();

            events.stream()
                    .filter(event -> {
                        VirtualFile file = event.getFile();
                        if (file == null) return false;
                        return event.getFile().toNioPath().toAbsolutePath().equals(Path.of(storageManager.getRuleset()).toAbsolutePath());
                    })
                    .findFirst()
                    .ifPresent(event -> VfsUtil.markDirtyAndRefresh(true, true, true, contentRoots));
        } catch (ProjectSettingsException e) {
            return null;
        }

        return null;
    }
}
