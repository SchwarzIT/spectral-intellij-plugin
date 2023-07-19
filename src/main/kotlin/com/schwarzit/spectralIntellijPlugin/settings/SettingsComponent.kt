package com.schwarzit.spectralIntellijPlugin.settings

import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import javax.swing.JPanel

class SettingsComponent {

    val mainPanel: JPanel
    val rulesetInput = JBTextField()

    init {
        mainPanel =
            FormBuilder.createFormBuilder()
                .addLabeledComponent(JBLabel("Spectral ruleset"), rulesetInput)
                .addComponentFillVertically(JPanel(), 0)
                .panel
    }


}
