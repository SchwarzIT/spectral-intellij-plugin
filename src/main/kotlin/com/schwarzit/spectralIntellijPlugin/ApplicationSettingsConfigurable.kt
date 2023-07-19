package com.schwarzit.spectralIntellijPlugin

import com.intellij.openapi.options.Configurable
import javax.swing.JComponent

class ApplicationSettingsConfigurable : Configurable {

    private lateinit var settingsComponent: AppSettingsComponent

    override fun createComponent(): JComponent {
        settingsComponent = AppSettingsComponent()
        return settingsComponent.mainPanel
    }

    override fun getPreferredFocusedComponent(): JComponent {
        return settingsComponent.rulesetInput
    }

    override fun isModified(): Boolean {
        val settings = ApplicationSettingsState.instance
        return settingsComponent.rulesetInput.text != settings.state.ruleset
    }

    override fun apply() {
        val state = ApplicationSettingsState.instance.state
        state.ruleset = settingsComponent.rulesetInput.text
    }

    override fun reset() {
        val state = ApplicationSettingsState.instance
        settingsComponent.rulesetInput.text = state.ruleset
    }

    override fun getDisplayName(): String {
        return "Spectral"
    }

}