package com.schwarzit.spectralIntellijPlugin

import com.intellij.lang.annotation.HighlightSeverity
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class SpectralIssue(
    val code: String,
    // the elements aren't always strings and can be numeric
    val path: List<JsonElement>,
    val message: String,
    val severity: Int,
    val source: String,
    val range: ErrorRange
)


@Serializable
data class ErrorRange(
    val start: ErrorPosition,
    val end: ErrorPosition
)

@Serializable
data class ErrorPosition(
    val line: Int,
    val character: Int
)

fun mapSeverity(severity: Int): HighlightSeverity {
    return when (severity) {
        0 -> HighlightSeverity.ERROR
        1 -> HighlightSeverity.WARNING
        2 -> HighlightSeverity.WEAK_WARNING
        3 -> HighlightSeverity.INFORMATION
        else -> HighlightSeverity.ERROR
    }
}