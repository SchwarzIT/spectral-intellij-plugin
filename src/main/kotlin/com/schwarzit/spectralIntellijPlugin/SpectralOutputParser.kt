package com.schwarzit.spectralIntellijPlugin

import com.intellij.openapi.components.Service
import java.text.ParseException
import kotlin.jvm.Throws

@Service
class SpectralOutputParser {
    companion object {
        val logger = getLogger()
    }

    @Throws(ParseException::class)
    fun parse(input: String): List<SpectralIssue> {
        logger.info("Received output:\n$input")
        return emptyList()
    }
}
