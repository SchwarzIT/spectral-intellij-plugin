package com.schwarzit.spectralIntellijPlugin

import com.intellij.json.psi.JsonFile
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.ExternalAnnotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.impl.BackgroundableProcessIndicator
import com.intellij.openapi.util.Computable
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.schwarzit.spectralIntellijPlugin.settings.ProjectSettingsState
import org.apache.commons.io.FilenameUtils
import org.jetbrains.yaml.psi.YAMLFile
import java.io.File
import java.nio.file.Path

class SpectralExternalAnnotator : ExternalAnnotator<Pair<PsiFile, Editor>, List<SpectralIssue>>() {
    companion object {
        val logger = getLogger()
    }

    override fun collectInformation(file: PsiFile, editor: Editor, hasErrors: Boolean): Pair<PsiFile, Editor>? {
        if (file !is JsonFile && file !is YAMLFile) return null

        val settings = editor.project?.service<ProjectSettingsState>()
        val includedFiles = settings?.includedFiles?.lines() ?: emptyList()

        try {
            if (!isFileIncluded(
                    file.virtualFile.toNioPath(),
                    includedFiles
                )
            ) {
                logger.trace("The given file ${file.virtualFile.toNioPath()} did not match any pattern defined in the settings")
                return null
            }
        } catch (e: Throwable) {
            logger.error("Failed to check if current file is included. Parameters: basePath: ${file.project.basePath}, path: ${file.virtualFile.toNioPath()}, includedFiles: $includedFiles")
            return null
        }

        return Pair(file, editor)
    }

    fun isFileIncluded(path: Path, includedFiles: List<String>, separator: String = File.separator): Boolean {
        val finalPath = normalizedStringPath(path.toString(), separator)

        return includedFiles.any { s ->
            if (s.isEmpty()) return false
            val finalGlobPattern = normalizedStringPath(s, separator)
            return FilenameUtils.wildcardMatch(finalPath, finalGlobPattern)
        }
    }

    private fun normalizedStringPath(path: String, separator: String): String {
        return path.replace('\\', separator[0]).replace('/', separator[0])
    }

    override fun doAnnotate(info: Pair<PsiFile, Editor>): List<SpectralIssue> {
        val progressManager = ProgressManager.getInstance()
        val computable = Computable { lintFile(info.second) }
        val indicator = BackgroundableProcessIndicator(
            info.second.project,
            "Spectral: analyzing OpenAPI specification '${info.first.name}' ...",
            "Stop",
            "Stop file analysis",
            false
        )

        return progressManager.runProcess(computable, indicator)
    }

    private fun lintFile(editor: Editor): List<SpectralIssue> {
        val project = editor.project
        if (project == null) {
            logger.error("Unable to lint file, editor.project was null")
            return emptyList()
        }

        val linter = project.service<SpectralRunner>()
        return try {
            val issues = linter.run(editor.document)
            issues
        } catch (e: Throwable) {
            logger.error(e)
            emptyList()
        }
    }

    override fun apply(file: PsiFile, issues: List<SpectralIssue>?, holder: AnnotationHolder) {
        val documentManager = PsiDocumentManager.getInstance(file.project)
        val document = documentManager.getDocument(file) ?: return

        if (issues == null) return

        val fileLevelIssueCodes = listOf("unrecognized-format", "parser-error")
        if (issues.any { issue -> fileLevelIssueCodes.contains(issue.code) }) {
            logger.warn("Linted openapi spec is not valid: Skipping linting")
            holder.newAnnotation(HighlightSeverity.WARNING, issues.first().message)
                .fileLevel()
                .create()
            return
        }

        for (issue in issues) {
            var textRange: TextRange
            try {
                textRange = calculateIssueTextRange(document, issue.range)
                if (issue.range.start.line == 0) {
                    holder
                        .newAnnotation(
                            mapSeverity(issue.severity),
                            issue.code + ": " + issue.message
                        )
                        .range(textRange)
                        .fileLevel()
                        .create()
                } else {

                    holder
                        .newAnnotation(
                            mapSeverity(issue.severity),
                            issue.code + ": " + issue.message
                        )
                        .range(textRange)
                        .create()
                }
            } catch (e: Throwable) {
                continue
            }
        }
    }

    private fun calculateIssueTextRange(document: Document, range: ErrorRange): TextRange {
        val lineStartOffset = document.getLineStartOffset(range.start.line)
        val lineEndOffset = document.getLineStartOffset(range.start.line) + range.start.character - 1

        val startLine = TextRange(lineStartOffset, lineEndOffset)
        val lineWhiteSpaceCount = countLeadingWhiteSpace(document.getText(startLine))
        return TextRange(lineStartOffset + lineWhiteSpaceCount, lineEndOffset)
    }

    private fun countLeadingWhiteSpace(line: String): Int {
        var count = 0
        for (char in line) {
            if (Character.isWhitespace(char)) {
                count++
            } else {
                break
            }
        }
        return count
    }
}
