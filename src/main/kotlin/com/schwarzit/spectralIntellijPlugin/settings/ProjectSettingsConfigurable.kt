package com.schwarzit.spectralIntellijPlugin.settings

import com.intellij.openapi.components.service
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import javax.swing.JComponent

class ProjectSettingsConfigurable(private val project: Project) : Configurable {

    private lateinit var settingsComponent: SettingsComponent

    override fun createComponent(): JComponent {
        val settings = project.service<ProjectSettingsState>()
        settingsComponent = SettingsComponent()

        settingsComponent.rulesetInput.text = settings.ruleset
        settingsComponent.includedFilesInput.text = settings.includedFiles
        return settingsComponent.mainPanel
    }

    override fun getPreferredFocusedComponent(): JComponent {
        return settingsComponent.rulesetInput
    }

    override fun isModified(): Boolean {
        val settings = project.service<ProjectSettingsState>()
        var modified = settingsComponent.rulesetInput.text != settings.ruleset
        modified = modified || settingsComponent.includedFilesInput.text != settings.includedFiles
        return modified
    }

    override fun apply() {
        val settings = project.service<ProjectSettingsState>()
        settings.ruleset = settingsComponent.rulesetInput.text
        settings.includedFiles = settingsComponent.includedFilesInput.text
    }

    override fun getDisplayName(): String {
        return "Spectral Linter"
    }
}