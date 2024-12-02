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
    fun isFileIncluded(
        basePath: String,
        path: String,
        includedFiles: List<String>,
        isIncluded: Boolean,
        separator: String
    ) {
        val spectralExternalAnnotator = SpectralExternalAnnotator()
        val fileIncluded =
            spectralExternalAnnotator.isFileIncluded(Paths.get(path), includedFiles, separator)
        Assertions.assertEquals(isIncluded, fileIncluded)
    }

    companion object {
        @JvmStatic
        fun provideIsFileIncludedTestParams(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(
                    "/home/user/project",
                    "/home/user/project/src/openapi.json",
                    listOf("**/openapi.json"),
                    true,
                    "/"
                ),
                Arguments.of(
                    "C:\\Users\\Username\\Projekt\\",
                    "C:\\Users\\Username\\Projekt\\API_Name.yaml",
                    listOf("**.yaml"),
                    true,
                    "\\"
                ),
                Arguments.of("/test", "/test/testing.test", listOf("*.test"), true, "/"),
                Arguments.of("/", "/foo/bar/something/test.json", listOf("**/*.json"), true, "/"),
                Arguments.of("/", "test.test", listOf("*.json", "*.yml"), false, "/"),
                Arguments.of("/", "/test/openapi.json", listOf("*.json"), true, "/"),
                Arguments.of("/", "/test/openapi.json", listOf("*/test/**.json"), true, "/")
            )
        }
    }

}