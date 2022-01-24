package com.schwarzit.spectralIntellijPlugin.models;

import com.intellij.lang.annotation.HighlightSeverity;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SpectralIssueTest {

    public static Stream<Arguments> mapSeverityData() {
        return Stream.of(
                Arguments.of(0, HighlightSeverity.ERROR),
                Arguments.of(1, HighlightSeverity.WARNING),
                Arguments.of(2, HighlightSeverity.WEAK_WARNING),
                Arguments.of(3, HighlightSeverity.INFORMATION),
                Arguments.of(4, HighlightSeverity.ERROR),
                Arguments.of(-42, HighlightSeverity.ERROR)
        );
    }

    @ParameterizedTest
    @MethodSource("mapSeverityData")
    void testMapSeverity(int severity, HighlightSeverity expectedSeverity) {
        HighlightSeverity highlightSeverity = SpectralIssue.mapSeverity(severity);
        assertEquals(expectedSeverity, highlightSeverity);
    }
}