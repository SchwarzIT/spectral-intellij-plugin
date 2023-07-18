package com.schwarzit.spectralIntellijPlugin

import com.intellij.json.psi.JsonFile
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.ExternalAnnotator
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.impl.BackgroundableProcessIndicator
import com.intellij.openapi.util.Computable
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile

class SpectralExternalAnnotator : ExternalAnnotator<Editor, List<SpectralIssue>>() {

    companion object {
        val logger = getLogger()
    }

    override fun collectInformation(file: PsiFile, editor: Editor, hasErrors: Boolean): Editor? {
        if (file !is JsonFile) return null
        return editor
    }

    override fun doAnnotate(editor: Editor): List<SpectralIssue> {
        val progressManager = ProgressManager.getInstance()
        val computable = Computable { lintFile(editor) }
        val indicator = BackgroundableProcessIndicator(
            editor.project,
            "Spectral: analyzing OpenAPI spec...",
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
            val issues = linter.run(editor.document.text)
            logger.warn(issues.toString())
            issues
        } catch (e: SpectralException) {
            emptyList()
        }
    }

    override fun apply(file: PsiFile, issues: List<SpectralIssue>?, holder: AnnotationHolder) {
        val documentManager = PsiDocumentManager.getInstance(file.project)
        val document = documentManager.getDocument(file) ?: return

        issues?.forEach { issue ->
            holder
                .newAnnotation(
                    mapSeverity(issue.severity),
                    issue.code + ": " + issue.message
                )
                .range(calculateIssueTextRange(document, issue.range))
                .create()
        }
    }

    private fun calculateIssueTextRange(document: Document, range: ErrorRange): TextRange {
        val startOffset = document.getLineStartOffset(range.start.line) + range.start.character
        val endOffset = document.getLineStartOffset(range.end.line) + range.end.character

        return TextRange(startOffset, endOffset)
    }
}
