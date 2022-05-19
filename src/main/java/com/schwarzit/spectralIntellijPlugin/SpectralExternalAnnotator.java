package com.schwarzit.spectralIntellijPlugin;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.ExternalAnnotator;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.PsiFile;
import com.schwarzit.spectralIntellijPlugin.exceptions.ProjectSettingsException;
import com.schwarzit.spectralIntellijPlugin.exceptions.SpectralException;
import com.schwarzit.spectralIntellijPlugin.models.SpectralIssue;
import com.schwarzit.spectralIntellijPlugin.settings.BaseSettingsState;
import com.schwarzit.spectralIntellijPlugin.settings.ProjectSettingsState;
import com.schwarzit.spectralIntellijPlugin.util.NotificationHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.schwarzit.spectralIntellijPlugin.models.SpectralIssue.mapSeverity;

public class SpectralExternalAnnotator extends ExternalAnnotator<PsiFile, List<SpectralIssue>> {
    private final static Logger logger = Logger.getInstance("Spectral");

    private final SpectralRunner spectralRunner;
    private final BaseSettingsState projectSettingsState;
    private final NotificationHandler notificationHandler;

    @SuppressWarnings("unused")
    public SpectralExternalAnnotator() throws ProjectSettingsException {
        this.notificationHandler = NotificationHandler.getInstance();
        this.projectSettingsState = ProjectSettingsState.getInstance();

        try {
            spectralRunner = new SpectralRunner(StorageManager.getInstance());
        } catch (SpectralException e) {
            notificationHandler.showNotification("Spectral installation failed", e.getMessage(), NotificationType.ERROR);
            throw new RuntimeException("Spectral installation failed", e);
        }
    }

    public SpectralExternalAnnotator(SpectralRunner spectralRunner, BaseSettingsState projectSettingsState, NotificationHandler notificationHandler) {
        this.spectralRunner = spectralRunner;
        this.projectSettingsState = projectSettingsState;
        this.notificationHandler = notificationHandler;
    }

    @Override
    public @Nullable PsiFile collectInformation(@NotNull PsiFile file) {
        @NotNull Path path = file.getVirtualFile().toNioPath();
        String[] globPatterns = projectSettingsState.getIncludedFiles().split("[;]");

        logger.debug("Matching " + path + " against the following glob patterns: " + Arrays.toString(globPatterns));
        boolean matches = Arrays.stream(globPatterns).map(String::trim).anyMatch(pattern -> {
            PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
            return pathMatcher.matches(path);
        });
        if (!matches) {
            logger.debug("File " + path + " did not match any of the glob patterns");
            return null;
        }

        return file;
    }

    @Override
    public @Nullable List<SpectralIssue> doAnnotate(PsiFile file) {
        Path filePath = file.getVirtualFile().toNioPath();
        Project project = file.getProject();
        Document document = file.getViewProvider().getDocument();
        String rulesetPath = projectSettingsState.getRuleset();

        try {
            VirtualFileManager.getInstance().refreshWithoutFileWatcher(true);

            logger.info("Stated linting file: " + filePath);

            List<SpectralIssue> spectralIssues = spectralRunner.lint(filePath, rulesetPath);
            spectralIssues.forEach(issue -> issue.setDocument(document));

            logger.info("Finished linting file: " + file.getVirtualFile().toNioPath());

            return spectralIssues;
        } catch (SpectralException e) {
            notificationHandler.showNotification("Linting failed", e.getMessage(), NotificationType.WARNING, project);
        }

        return Collections.emptyList();
    }

    @Override
    public void apply(@NotNull PsiFile file, List<SpectralIssue> issues, @NotNull AnnotationHolder holder) {
        issues.forEach(issue -> holder.newAnnotation(
                                mapSeverity(issue.getSeverity()),
                                issue.getCode() + ": " + issue.getMessage()
                        )
                        .range(issue.getRange().getTextRange())
                        .create()
        );
    }
}
