package com.schwarzit.spectralIntellijPlugin.models;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;

import java.util.Objects;

public class ErrorRange {
    private Document document;
    private final ErrorPosition start;
    private final ErrorPosition end;

    public ErrorRange(ErrorPosition start, ErrorPosition end) {
        this.start = start;
        this.end = end;
    }

    public TextRange getTextRange() {
        int startOffset = this.document.getLineStartOffset(start.getLine()) + start.getCharacter();
        int endOffset = this.document.getLineStartOffset(end.getLine()) + end.getCharacter();

        return new TextRange(startOffset, endOffset);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorRange range = (ErrorRange) o;
        return start.equals(range.start) && end.equals(range.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    @Override
    public String toString() {
        return "ErrorRange{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }

    public void setDocument(Document document) {
        this.document = document;
    }
}
