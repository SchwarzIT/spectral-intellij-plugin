package com.schwarzit.spectralIntellijPlugin;

import com.intellij.lang.annotation.AnnotationBuilder;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiFile;
import com.schwarzit.spectralIntellijPlugin.exceptions.SpectralException;
import com.schwarzit.spectralIntellijPlugin.exceptions.TempFileException;
import com.schwarzit.spectralIntellijPlugin.models.ErrorPosition;
import com.schwarzit.spectralIntellijPlugin.models.ErrorRange;
import com.schwarzit.spectralIntellijPlugin.models.SpectralIssue;
import com.schwarzit.spectralIntellijPlugin.settings.BaseSettingsState;
import com.schwarzit.spectralIntellijPlugin.util.NotificationHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class SpectralExternalAnnotatorTest {

    private SpectralRunner spectralRunnerMock;
    private BaseSettingsState settingsStateMock;
    private NotificationHandler notificationHandlerMock;
    private SpectralExternalAnnotator spectralExternalAnnotator;

    @BeforeEach
    void setUp() {
        spectralRunnerMock = mock(SpectralRunner.class);
        settingsStateMock = mock(BaseSettingsState.class);
        notificationHandlerMock = mock(NotificationHandler.class);
        spectralExternalAnnotator = new SpectralExternalAnnotator(spectralRunnerMock, settingsStateMock, notificationHandlerMock);
    }

    public static Stream<Arguments> collectInformationData() {
        return Stream.of(
                Arguments.of("**/*.json", "/openapi/openapi.json", true),
                Arguments.of("*.json", "/openapi/openapi.json", false),
                Arguments.of("*.json", "openapi.json", true),
                Arguments.of("*/*.json", "openapi/openapi.json", true),
                Arguments.of("**/*.json", "/openapi.json", true),
                Arguments.of("**/*.json", "/openapi/openapi.yml", false),
                Arguments.of("**test.*", "/openapi/test.yml", true),
                Arguments.of("**.{json,yml}", "openapi.yml", true),
                Arguments.of("**.{json,yml}", "openapi.json", true),
                Arguments.of("**.{json,y*ml}", "openapi.yaml", true),
                Arguments.of("**.{json,y*ml}", "openapi.yml", true),
                Arguments.of("**.json;**.yml", "openapi.json", true),
                Arguments.of("**.json; **.yml", "openapi.yml", true),
                Arguments.of("**.json; **.yml", "openapi.txt", false)
        );
    }

    @ParameterizedTest(name = "{index}: glob:{0} matches {1} = {2}")
    @MethodSource("collectInformationData")
    void testCollectInformation(String globPattern, String filePath, Boolean matches) {
        PsiFile psiFileMock = mock(PsiFile.class);
        VirtualFile virtualFileMock = mock(VirtualFile.class);
        when(virtualFileMock.toNioPath()).thenReturn(Path.of(filePath));
        when(psiFileMock.getVirtualFile()).thenReturn(virtualFileMock);
        when(settingsStateMock.getIncludedFiles()).thenReturn(globPattern);

        PsiFile result = spectralExternalAnnotator.collectInformation(psiFileMock);

        if (matches) {
            assertEquals(psiFileMock, result, "Filepath did not match glob pattern but should");
        } else {
            assertNull(result, "Filepath did match glob pattern but shouldn't");
        }
    }

    @Test
    void testDoAnnotate() throws SpectralException, TempFileException {
        SpectralIssue issue = spy(new SpectralIssue("code", new String[]{"path"}, "message", 0, "source",
                new ErrorRange(
                        new ErrorPosition(0, 0),
                        new ErrorPosition(42, 42)
                )
        ));
        List<SpectralIssue> issues = List.of(issue);

        PsiFile psiFileMock = mock(PsiFile.class);
        VirtualFile virtualFileMock = mock(VirtualFile.class);
        when(psiFileMock.getVirtualFile()).thenReturn(virtualFileMock);
        when(virtualFileMock.toNioPath()).thenReturn(Path.of("TestPath"));
        when(psiFileMock.getViewProvider()).thenReturn(mock(FileViewProvider.class));
        when(spectralRunnerMock.lint(any(), any())).thenReturn(issues);

        try (MockedStatic<VirtualFileManager> vfmMockStatic = mockStatic(VirtualFileManager.class)) {
            vfmMockStatic.when(VirtualFileManager::getInstance).thenReturn(mock(VirtualFileManager.class));

            List<SpectralIssue> spectralIssues = spectralExternalAnnotator.doAnnotate(psiFileMock);

            assertEquals(issues, spectralIssues);
            verify(issue, times(1)).setDocument(any());
        }
    }

    @Test
    void testDoAnnotateSpectralException() throws SpectralException, TempFileException {
        PsiFile psiFileMock = mock(PsiFile.class);
        VirtualFile virtualFileMock = mock(VirtualFile.class);
        when(psiFileMock.getVirtualFile()).thenReturn(virtualFileMock);
        when(virtualFileMock.toNioPath()).thenReturn(Path.of("TestPath"));
        when(psiFileMock.getViewProvider()).thenReturn(mock(FileViewProvider.class));
        when(spectralRunnerMock.lint(any(), any())).thenThrow(new SpectralException("Spectral exception"));

        try (MockedStatic<VirtualFileManager> vfmMockStatic = mockStatic(VirtualFileManager.class)) {
            vfmMockStatic.when(VirtualFileManager::getInstance).thenReturn(mock(VirtualFileManager.class));

            List<SpectralIssue> spectralIssues = spectralExternalAnnotator.doAnnotate(psiFileMock);

            assertEquals(Collections.emptyList(), spectralIssues);
            verify(notificationHandlerMock).showNotification(eq("Linting failed"), any(), eq(NotificationType.WARNING), any());
        }
    }

    @Test
    void testApply() {
        TextRange textRange = new TextRange(0, 42);
        PsiFile psiFileMock = mock(PsiFile.class);
        AnnotationHolder annotationHolderMock = mock(AnnotationHolder.class);
        AnnotationBuilder annotationBuilderMock = mock(AnnotationBuilder.class);
        ErrorRange errorRangeMock = mock(ErrorRange.class);
        SpectralIssue issue = new SpectralIssue("code", new String[]{"path"}, "Message", 0, "source", errorRangeMock);
        List<SpectralIssue> issues = List.of(issue);

        when(annotationHolderMock.newAnnotation(any(), any())).thenReturn(annotationBuilderMock);
        when(annotationBuilderMock.range(isA(TextRange.class))).thenReturn(annotationBuilderMock);
        when(errorRangeMock.getTextRange()).thenReturn(textRange);

        spectralExternalAnnotator.apply(psiFileMock, issues, annotationHolderMock);

        verify(annotationHolderMock).newAnnotation(HighlightSeverity.ERROR, "Message");
        verify(annotationBuilderMock).range(textRange);
        verify(annotationBuilderMock).create();
    }
}