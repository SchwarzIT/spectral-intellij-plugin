package com.schwarzit.spectralIntellijPlugin.util;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.project.Project;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Field;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NotificationHandlerTest {
    private NotificationHandler notificationHandler;

    @BeforeEach
    void setUp() {
        notificationHandler = NotificationHandler.getInstance();
        notificationHandler.setProject(mock(Project.class));
    }

    @Test
    void showNotification() {
        try (MockedStatic<Notifications.Bus> busMockedStatic = Mockito.mockStatic(Notifications.Bus.class)) {
            try (MockedConstruction<Notification> ignored = Mockito.mockConstruction(Notification.class, (mock, context) -> when(mock.setIcon(any())).thenReturn(mock))) {

                busMockedStatic.when(() -> Notifications.Bus.notify(any(), any())).thenAnswer(x -> null);
                notificationHandler.showNotification("title", "content", NotificationType.ERROR);
                busMockedStatic.verify(() -> Notifications.Bus.notify(any(), notNull()));
            }
        }
    }

    @AfterEach
    void tearDown() throws NoSuchFieldException, IllegalAccessException {
        Field instance = NotificationHandler.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }
}