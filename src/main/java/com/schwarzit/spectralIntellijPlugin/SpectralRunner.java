package com.schwarzit.spectralIntellijPlugin;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.ScriptRunnerUtil;
import com.schwarzit.spectralIntellijPlugin.exceptions.SpectralException;
import com.schwarzit.spectralIntellijPlugin.exceptions.TempFileException;
import com.schwarzit.spectralIntellijPlugin.models.SpectralIssue;

import java.nio.file.Path;
import java.util.List;

public class SpectralRunner {
    private final StorageManager storageManager;

    public SpectralRunner(StorageManager storageManager) {
        this.storageManager = storageManager;
    }

    public List<SpectralIssue> lint(Path fileContent, String rulesetPath) throws TempFileException, SpectralException {
        GeneralCommandLine cli = new GeneralCommandLine(storageManager.getSpectralExecutable().getAbsolutePath());
        cli.setWorkDirectory(storageManager.getStoragePath().toString());
        cli.setRedirectErrorStream(true);
        cli.addParameters("lint", "--format", "json", "--ruleset", rulesetPath, fileContent.toAbsolutePath().toString());

        String processOutput = "";
        try {
            processOutput = ScriptRunnerUtil.getProcessOutput(cli);
            // Removing everything before the first '[' and after the last ']' since spectral outputs additional text if no issues were found
            processOutput = processOutput.substring(processOutput.indexOf('['), processOutput.lastIndexOf(']') + 1);

            SpectralIssue[] issues = parseSpectralResponse(processOutput);
            return List.of(issues);
        } catch (ExecutionException | JsonSyntaxException | IndexOutOfBoundsException e) {
            throw new SpectralException("Execution of Spectral failed using the following command: \"" + cli.getCommandLineString() + "\"\nError Message:\n" + processOutput, e);
        }
    }

    private SpectralIssue[] parseSpectralResponse(String json) throws JsonSyntaxException {
        return new Gson().fromJson(json, SpectralIssue[].class);
    }
}
