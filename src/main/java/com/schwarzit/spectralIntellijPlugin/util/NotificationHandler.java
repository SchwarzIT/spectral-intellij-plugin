package com.schwarzit.spectralIntellijPlugin.util;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.project.Project;
import icons.SpectralIcons;
import org.jetbrains.annotations.Nullable;

public class NotificationHandler {
    private static NotificationHandler instance;
    private Project project;

    public void setProject(Project project) {
        this.project = project;
    }

    public void showNotification(String title, String content, NotificationType type, @Nullable Project project) {
        Notification notification = new Notification("Spectral", title, content, type);
        notification.setIcon(SpectralIcons.SpectralLogo);
        Notifications.Bus.notify(notification, project);
    }

    public void showNotification(String title, String content, NotificationType type) {
        showNotification(title, content, type, project);
    }

    public static NotificationHandler getInstance() {
        if (instance == null) {
            instance = new NotificationHandler();
        }
        return instance;
    }
}
