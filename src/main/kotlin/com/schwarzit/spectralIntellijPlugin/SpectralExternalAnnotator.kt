package com.schwarzit.spectralIntellijPlugin;

import com.intellij.json.psi.JsonFile
import com.intellij.lang.annotation.ExternalAnnotator
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.impl.BackgroundableProcessIndicator
import com.intellij.openapi.util.Computable
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.psi.PsiFile

class SpectralExternalAnnotator : ExternalAnnotator<Editor, List<SpectralIssue>>() {
    override fun collectInformation(file: PsiFile, editor: Editor, hasErrors: Boolean): Editor? {
        if (file !is JsonFile) return null
        return editor
    }

    override fun doAnnotate(editor: Editor): List<SpectralIssue> {
        val progressManager = ProgressManager.getInstance()
        val computable = Computable { lintFile(editor) }
        val indicator = BackgroundableProcessIndicator(
            editor.project,
            "Spectral: Analyzing text in editor...",
            "Stop",
            "Stop file analysis",
            false
        )

        return progressManager.runProcess(computable, indicator)
    }

    private fun lintFile(editor: Editor): List<SpectralIssue> {
        val project = editor.project
        if (project == null) return emptyList() // ToDo: Handle error more transparently

        val linter = project.service<SpectralRunner>()

        try {
            return linter.run(editor.document.text)
        } catch (e: SpectralException) {
            return emptyList()
        }
    }
}
