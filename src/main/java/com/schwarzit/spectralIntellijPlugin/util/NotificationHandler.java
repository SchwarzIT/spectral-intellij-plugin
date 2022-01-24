package com.schwarzit.spectralIntellijPlugin.util;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.project.Project;
import icons.SpectralIcons;
import org.jetbrains.annotations.Nullable;

public class NotificationHandler {
    private static Project project;

    public static void initialize(Project project) {
        NotificationHandler.project = project;
    }

    public static void showNotification(String title, String content, NotificationType type, @Nullable Project project) {
        Notification notification = new Notification("Spectral", title, content, type);
        notification.setIcon(SpectralIcons.SpectralLogo);
        Notifications.Bus.notify(notification, project);
    }

    public static void showNotification(String title, String content, NotificationType type) {
        NotificationHandler.showNotification(title, content, type, project);
    }
}
