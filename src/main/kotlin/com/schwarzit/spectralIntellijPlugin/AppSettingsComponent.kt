package com.schwarzit.spectralIntellijPlugin

import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import javax.swing.JPanel

class AppSettingsComponent {

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
