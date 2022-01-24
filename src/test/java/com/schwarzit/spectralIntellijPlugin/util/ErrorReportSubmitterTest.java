package com.schwarzit.spectralIntellijPlugin.util;

import com.intellij.ide.BrowserUtil;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.diagnostic.IdeaLoggingEvent;
import com.intellij.util.EmptyConsumer;
import com.schwarzit.spectralIntellijPlugin.config.Config;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

import java.awt.*;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class ErrorReportSubmitterTest {
    private NotificationHandler notificationHandler;
    private ErrorReportSubmitter errorReportSubmitter;

    @BeforeEach
    void setUp() {
        notificationHandler = mock(NotificationHandler.class);
        doNothing().when(notificationHandler).showNotification(any(), any(), any());
        errorReportSubmitter = new ErrorReportSubmitter(notificationHandler, Config.Instance.ERROR_REPORTING_URL());
    }

    @Test
    void testSubmit() {
        try (MockedStatic<BrowserUtil> browserUtilMockedStatic = mockStatic(BrowserUtil.class)) {
            boolean submit = errorReportSubmitter.submit(
                    new IdeaLoggingEvent[0],
                    "additionalInfo",
                    mock(Component.class),
                    EmptyConsumer.getInstance()
            );

            assertTrue(submit);
            verify(notificationHandler, never()).showNotification(anyString(), anyString(), any());

            ArgumentCaptor<URI> uriArgumentCaptor = ArgumentCaptor.forClass(URI.class);
            browserUtilMockedStatic.verify(() -> BrowserUtil.browse(uriArgumentCaptor.capture()), times(1));
            assertTrue(uriArgumentCaptor.getValue().toString().contains("github"));
        }
    }

    @Test
    void testSubmitNoAdditionalInfo() {
        try (MockedStatic<BrowserUtil> browserUtilMockedStatic = mockStatic(BrowserUtil.class)) {
            boolean submit = errorReportSubmitter.submit(
                    new IdeaLoggingEvent[0],
                    null,
                    mock(Component.class),
                    EmptyConsumer.getInstance()
            );

            assertTrue(submit);
            verify(notificationHandler, never()).showNotification(anyString(), anyString(), any());

            ArgumentCaptor<URI> uriArgumentCaptor = ArgumentCaptor.forClass(URI.class);
            browserUtilMockedStatic.verify(() -> BrowserUtil.browse(uriArgumentCaptor.capture()), times(1));
            assertTrue(uriArgumentCaptor.getValue().getQuery().contains(URLEncoder.encode("Please provide a detailed description", StandardCharsets.UTF_8)));
        }
    }

    @Test
    void testSubmitUriException() {
        ErrorReportSubmitter errorReportSubmitter = new ErrorReportSubmitter(notificationHandler, "invalid Url");

        try (MockedStatic<BrowserUtil> browserUtilMockedStatic = mockStatic(BrowserUtil.class)) {
            boolean submit = errorReportSubmitter.submit(
                    new IdeaLoggingEvent[0],
                    null,
                    mock(Component.class),
                    EmptyConsumer.getInstance()
            );

            assertFalse(submit);
            verify(notificationHandler).showNotification(anyString(), contains("invalid Url"), eq(NotificationType.ERROR));
            browserUtilMockedStatic.verify(() -> BrowserUtil.browse(isA(URI.class)), never());
        }
    }
}