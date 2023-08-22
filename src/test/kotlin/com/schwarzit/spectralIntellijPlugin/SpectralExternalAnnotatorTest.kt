package com.schwarzit.spectralIntellijPlugin

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.nio.file.Paths
import java.util.stream.Stream

class SpectralExternalAnnotatorTest {

    @ParameterizedTest(name = "isFileIncluded {index} - {arguments}")
    @MethodSource("provideIsFileIncludedTestParams")
    fun isFileIncluded(basePath: String, path: String, includedFiles: List<String>, isIncluded: Boolean) {
        val spectralExternalAnnotator = SpectralExternalAnnotator()
        val fileIncluded = spectralExternalAnnotator.isFileIncluded(basePath, Paths.get(path), includedFiles)
        Assertions.assertEquals(fileIncluded, isIncluded)
    }

    companion object {
        @JvmStatic
        fun provideIsFileIncludedTestParams(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(
                    "/home/user/project",
                    "/home/user/project/src/openapi.json",
                    listOf("**openapi.json"),
                    true
                )
            )
        }
    }

}