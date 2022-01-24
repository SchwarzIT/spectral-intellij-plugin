package com.schwarzit.spectralIntellijPlugin.models;

import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.Document;

public class SpectralIssue {
    private final String code;
    private final String[] path;
    private final String message;
    private final int severity;
    private final String source;
    private final ErrorRange range;

    @SuppressWarnings("unused")
    public SpectralIssue(String code, String[] path, String message, int severity, String source, ErrorRange range) {
        this.code = code;
        this.path = path;
        this.message = message;
        this.severity = severity;
        this.source = source;
        this.range = range;
    }

    public String getCode() {
        return code;
    }

    public String[] getPath() {
        return path;
    }

    public String getMessage() {
        return message;
    }

    public int getSeverity() {
        return severity;
    }

    public String getSource() {
        return source;
    }

    public ErrorRange getRange() {
        return range;
    }

    public static HighlightSeverity mapSeverity(int severity) {
        switch (severity) {
            case 0: // Error
                return HighlightSeverity.ERROR;
            case 1: // Warn
                return HighlightSeverity.WARNING;
            case 2: // Info
                return HighlightSeverity.WEAK_WARNING;
            case 3: // Hint
                return HighlightSeverity.INFORMATION;
            default: {
                return HighlightSeverity.ERROR;
            }
        }
    }

    public void setDocument(Document document) {
        this.range.setDocument(document);
    }
}