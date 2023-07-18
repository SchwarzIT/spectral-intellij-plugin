package com.schwarzit.spectralIntellijPlugin

import com.intellij.openapi.components.Service
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.text.ParseException

@Service
class SpectralOutputParser {
    @Throws(ParseException::class)
    fun parse(input: String): List<SpectralIssue> {
        return Json.decodeFromString<List<SpectralIssue>>(input)
    }
}
