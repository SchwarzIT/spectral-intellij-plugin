package com.schwarzit.spectralIntellijPlugin.util;

import com.intellij.ide.BrowserUtil;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.diagnostic.IdeaLoggingEvent;
import com.intellij.openapi.diagnostic.SubmittedReportInfo;
import com.intellij.openapi.util.NlsActions;
import com.intellij.util.Consumer;
import com.schwarzit.spectralIntellijPlugin.config.Config;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ErrorReportSubmitter extends com.intellij.openapi.diagnostic.ErrorReportSubmitter {
    private final NotificationHandler notificationHandler;
    private final String errorReportingURL;

    @SuppressWarnings("unused")
    public ErrorReportSubmitter() {
        this.notificationHandler = NotificationHandler.getInstance();
        this.errorReportingURL = Config.Instance.ERROR_REPORTING_URL();
    }

    public ErrorReportSubmitter(NotificationHandler notificationHandler, String errorReportingURL) {
        this.notificationHandler = notificationHandler;
        this.errorReportingURL = errorReportingURL;
    }

    @Override
    public @NlsActions.ActionText @NotNull String getReportActionText() {
        return "Create an Issue on Github";
    }

    @Override
    public boolean submit(IdeaLoggingEvent @NotNull [] events, @Nullable String additionalInfo, @NotNull Component parentComponent, @NotNull Consumer<? super SubmittedReportInfo> consumer) {
        try {
            if (additionalInfo == null || additionalInfo.trim().equals("")) {
                additionalInfo = "-> Please provide a detailed description of your issue and a way to reproduce it";
            }
            String body = String.format("**Description:**\n\n%s\n\n**Stacktraces:**\n\n%s", additionalInfo, Arrays.stream(events).map(ideaLoggingEvent -> "```\n" + ideaLoggingEvent.getThrowableText() + "\n```").collect(Collectors.joining("\n\n")));

            // Building an url to open, so the user can directly create an issue on GitHub
            // See: https://docs.github.com/en/issues/tracking-your-work-with-issues/creating-an-issue#creating-an-issue-from-a-url-query
            URI uri = new URIBuilder(errorReportingURL)
                    .addParameter("title", "")
                    .addParameter("body", body)
                    .build();

            BrowserUtil.browse(uri);
        } catch (URISyntaxException e) {
            notificationHandler.showNotification("Invalid error report uri", e.getMessage(), NotificationType.ERROR);
            return false;
        }

        return true;
    }
}
