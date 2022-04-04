package com.schwarzit.spectralIntellijPlugin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.newvfs.events.VFileEvent;
import com.schwarzit.spectralIntellijPlugin.exceptions.ProjectSettingsException;
import com.schwarzit.spectralIntellijPlugin.settings.ProjectSettingsState;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.nio.file.Path;
import java.util.List;

import static org.mockito.Mockito.*;

class FileListenerTest {

    @Test
    void prepareChangeTest() {
        try (MockedStatic<ProjectSettingsState> projectSettingsStateMockedStatic = mockStatic(ProjectSettingsState.class)) {
            ProjectSettingsState projectSettingsStateMock = mock(ProjectSettingsState.class);
            projectSettingsStateMockedStatic.when(ProjectSettingsState::getInstance).thenReturn(projectSettingsStateMock);

            try (MockedStatic<ProjectManager> projectManagerMockedStatic = mockStatic(ProjectManager.class)) {
                ProjectManager projectManagerMock = mock(ProjectManager.class);
                projectManagerMockedStatic.when(ProjectManager::getInstanceIfCreated).thenReturn(projectManagerMock);

                when(projectManagerMock.getDefaultProject()).thenReturn(mock(Project.class));

                try (MockedStatic<ProjectRootManager> projectRootManagerMockedStatic = mockStatic(ProjectRootManager.class)) {
                    ProjectRootManager projectRootManagerMock = mock(ProjectRootManager.class);
                    projectRootManagerMockedStatic.when(() -> ProjectRootManager.getInstance(any())).thenReturn(projectRootManagerMock);

                    VirtualFile[] virtualFiles = new VirtualFile[0];
                    when(projectRootManagerMock.getContentRoots()).thenReturn(virtualFiles);

                    try (MockedStatic<VfsUtil> vfsUtilMockedStatic = mockStatic(VfsUtil.class)) {
                        VFileEvent vFileEventMock = mock(VFileEvent.class);
                        VirtualFile virtualFileMock = mock(VirtualFile.class);
                        when(vFileEventMock.getFile()).thenReturn(virtualFileMock);
                        when(virtualFileMock.toNioPath()).thenReturn(Path.of("/TestPath"));
                        when(projectSettingsStateMock.getRuleset()).thenReturn("/TestPath");

                        //noinspection ResultOfMethodCallIgnored
                        new FileListener().prepareChange(List.of(vFileEventMock));

                        vfsUtilMockedStatic.verify(() -> VfsUtil.markDirtyAndRefresh(true, true, true, virtualFiles));
                    }
                }
            }
        }
    }

    @Test
    void prepareChangeWithoutMatchingFileTest() {
        try (MockedStatic<ProjectSettingsState> projectSettingsStateMockedStatic = mockStatic(ProjectSettingsState.class)) {
            ProjectSettingsState projectSettingsStateMock = mock(ProjectSettingsState.class);
            projectSettingsStateMockedStatic.when(ProjectSettingsState::getInstance).thenReturn(projectSettingsStateMock);

            try (MockedStatic<ProjectManager> projectManagerMockedStatic = mockStatic(ProjectManager.class)) {
                ProjectManager projectManagerMock = mock(ProjectManager.class);
                projectManagerMockedStatic.when(ProjectManager::getInstanceIfCreated).thenReturn(projectManagerMock);

                when(projectManagerMock.getDefaultProject()).thenReturn(mock(Project.class));

                try (MockedStatic<ProjectRootManager> projectRootManagerMockedStatic = mockStatic(ProjectRootManager.class)) {
                    ProjectRootManager projectRootManagerMock = mock(ProjectRootManager.class);
                    projectRootManagerMockedStatic.when(() -> ProjectRootManager.getInstance(any())).thenReturn(projectRootManagerMock);

                    VirtualFile[] virtualFiles = new VirtualFile[0];
                    when(projectRootManagerMock.getContentRoots()).thenReturn(virtualFiles);

                    try (MockedStatic<VfsUtil> vfsUtilMockedStatic = mockStatic(VfsUtil.class)) {
                        VFileEvent vFileEventMock = mock(VFileEvent.class);
                        VirtualFile virtualFileMock = mock(VirtualFile.class);
                        when(vFileEventMock.getFile()).thenReturn(virtualFileMock);
                        when(virtualFileMock.toNioPath()).thenReturn(Path.of("/DifferentTestPath"));
                        when(projectSettingsStateMock.getRuleset()).thenReturn("/TestPath");

                        //noinspection ResultOfMethodCallIgnored
                        new FileListener().prepareChange(List.of(vFileEventMock));

                        vfsUtilMockedStatic.verifyNoInteractions();
                    }
                }
            }
        }
    }

    @Test
    void prepareChangeWithUrlTest() {
        try (MockedStatic<ProjectSettingsState> projectSettingsStateMockedStatic = mockStatic(ProjectSettingsState.class)) {
            ProjectSettingsState projectSettingsStateMock = mock(ProjectSettingsState.class);
            projectSettingsStateMockedStatic.when(ProjectSettingsState::getInstance).thenReturn(projectSettingsStateMock);

            try (MockedStatic<VfsUtil> vfsUtilMockedStatic = mockStatic(VfsUtil.class)) {
                VFileEvent vFileEventMock = mock(VFileEvent.class);
                VirtualFile virtualFileMock = mock(VirtualFile.class);
                when(vFileEventMock.getFile()).thenReturn(virtualFileMock);
                when(virtualFileMock.toNioPath()).thenReturn(Path.of("/TestPath"));
                when(projectSettingsStateMock.getRuleset()).thenReturn("https://some.url/ruleset");

                //noinspection ResultOfMethodCallIgnored
                new FileListener().prepareChange(List.of(vFileEventMock));

                vfsUtilMockedStatic.verifyNoInteractions();
            }
        }
    }

    @Test
    void prepareChangeWithExceptionTest() {
        try (MockedStatic<ProjectSettingsState> projectSettingsStateMockedStatic = mockStatic(ProjectSettingsState.class)) {
            projectSettingsStateMockedStatic.when(ProjectSettingsState::getInstance).thenThrow(new ProjectSettingsException("Error"));

            try (MockedStatic<VfsUtil> vfsUtilMockedStatic = mockStatic(VfsUtil.class)) {
                VFileEvent vFileEventMock = mock(VFileEvent.class);
                VirtualFile virtualFileMock = mock(VirtualFile.class);
                when(vFileEventMock.getFile()).thenReturn(virtualFileMock);
                when(virtualFileMock.toNioPath()).thenReturn(Path.of("/DifferentTestPath"));
                //noinspection ResultOfMethodCallIgnored
                new FileListener().prepareChange(List.of(vFileEventMock));

                vfsUtilMockedStatic.verifyNoInteractions();
            }
        }
    }
}