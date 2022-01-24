package com.schwarzit.spectralIntellijPlugin.settings;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class BaseSettingsComponent {
    protected static Project project;

    protected final JPanel mainPanel;
    protected final JBTextField rulesetPath = new JBTextField();
    protected final JBTextField includedFiles = new JBTextField();

    public BaseSettingsComponent() {
        FileChooserDescriptor fileDescriptor = FileChooserDescriptorFactory.createSingleFileNoJarsDescriptor();
        TextFieldWithBrowseButton rulesetFileBrowser = new TextFieldWithBrowseButton(rulesetPath);
        rulesetFileBrowser.addBrowseFolderListener("Path to Ruleset", "Path to the .yml file containing the linting rules", project, fileDescriptor);

        mainPanel = FormBuilder.createFormBuilder()
                .addLabeledComponent(new JBLabel("Ruleset"), rulesetFileBrowser, 1, false)
                .addLabeledComponent(new JBLabel("Included files"), includedFiles, 1, false)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    public JComponent getPreferredFocusedComponent() {
        return rulesetPath;
    }

    public static void setProject(Project project) {
        BaseSettingsComponent.project = project;
    }

    @NotNull
    public String getRulesetPath() {
        return rulesetPath.getText();
    }

    public void setRulesetPath(@NotNull String rulesetPath) {
        this.rulesetPath.setText(rulesetPath);
    }

    public String getIncludedFiles() {
        return includedFiles.getText();
    }

    public void setIncludedFiles(@NotNull String includedFiles) {
        this.includedFiles.setText(includedFiles);
    }
}
