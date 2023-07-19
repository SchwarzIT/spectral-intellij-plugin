package com.schwarzit.spectralIntellijPlugin

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.schwarzit.spectralIntellijPlugin.settings.ApplicationSettingsState
import java.io.File
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.text.ParseException
import java.time.Duration
import java.util.concurrent.ExecutionException

@Service
class SpectralRunner(project: Project) {
    companion object {
        private val logger = getLogger()
        private val timeout = Duration.ofSeconds(30)
    }

    private val executor = project.service<CommandLineExecutor>()
    private val parser = project.service<SpectralOutputParser>()
    private val appSettings = service<ApplicationSettingsState>()

    fun run(content: String): List<SpectralIssue> {
        val tempFile = try {
            File.createTempFile("spectral-intellij-input-", ".tmp").apply { writeText(content) }
        } catch (e: IOException) {
            throw SpectralException("Failed to  create temporary file", e)
        }

        return try {
            createCommand()
                .withParameters("-r", appSettings.ruleset)
                .withParameters("-f", "json")
                .withParameters("lint")
                .withInput(tempFile)
                .execute()
        } finally {
            if (!tempFile.delete()) {
                logger.warn("Failed to delete temporary file ${tempFile.canonicalPath}")
            }
        }
    }

    private fun createCommand(): GeneralCommandLine {
        return GeneralCommandLine("spectral").withCharset(StandardCharsets.UTF_8)
    }

    @Throws(SpectralException::class)
    private fun GeneralCommandLine.execute(): List<SpectralIssue> {
        val output = try {
            executor.execute(this, timeout)
        } catch (e: ExecutionException) {
            throw SpectralException("Failed to execute command", e)
        }

        val successExitCodes = (0..2).toList()

        if (output.exitCode !in successExitCodes) {
            logger.error("Spectral error output: ${output.stderr}")
            throw SpectralException("Spectral finished with exit code ${output.exitCode} but expected one of $successExitCodes\n${output.stderr}")
        }

        if (output.stderr.isNotBlank()) {
            logger.error("Spectral error output: ${output.stderr}")
            throw SpectralException("An unexpected error occurred:\n${output.stderr}")
        }

        try {
            return parser.parse(output.stdout)
        } catch (e: ParseException) {
            throw SpectralException("Failed to parse output", e)
        }

    }
}