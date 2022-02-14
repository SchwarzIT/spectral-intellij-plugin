package com.schwarzit.spectralIntellijPlugin;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.ScriptRunnerUtil;
import com.schwarzit.spectralIntellijPlugin.exceptions.SpectralException;
import com.schwarzit.spectralIntellijPlugin.exceptions.TempFileException;
import com.schwarzit.spectralIntellijPlugin.models.SpectralIssue;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class SpectralRunner {
    private final StorageManager storageManager;

    public SpectralRunner(StorageManager storageManager) {
        this.storageManager = storageManager;
    }

    public List<SpectralIssue> lint(String fileContent, String rulesetPath) throws TempFileException, SpectralException {
        // It's necessary to create a temp file with the content of the virtual file, since the latest changes may not have been written to the actual file on disk
        Path tempFile = createTempFile(fileContent);

        GeneralCommandLine cli = new GeneralCommandLine(storageManager.getSpectralExecutable().getAbsolutePath());
        cli.setWorkDirectory(storageManager.getStoragePath().toString());
        cli.addParameters("lint", "--format", "json", "--ruleset", rulesetPath, tempFile.toAbsolutePath().toString());

        try {
            String processOutput = ScriptRunnerUtil.getProcessOutput(cli);
            // Removing everything before the first '[' and after the last ']' since spectral outputs additional text if no issues were found
            processOutput = processOutput.substring(processOutput.indexOf('['), processOutput.lastIndexOf(']') + 1);

            SpectralIssue[] issues = parseSpectralResponse(processOutput);
            return List.of(issues);
        } catch (ExecutionException | JsonSyntaxException | IndexOutOfBoundsException e) {
            throw new SpectralException("Execution of Spectral failed using the following command: \"" + cli.getCommandLineString() + "\"", e);
        } finally {
            try {
                Files.delete(tempFile);
            } catch (IOException e) {
                //noinspection ThrowFromFinallyBlock
                throw new TempFileException("Unable to delete tempFile", e);
            }
        }
    }

    /**
     * @param fileContent The content that will be written to the temp file
     * @return The path to the newly created temp file
     * @throws TempFileException if the temp file could not be created or written to
     */
    @NotNull
    private Path createTempFile(String fileContent) throws TempFileException {
        Path tempFile;
        try {
            tempFile = Files.createTempFile(null, null);
        } catch (IOException e) {
            throw new TempFileException("Unable to create temp file");
        }

        try (BufferedWriter out = Files.newBufferedWriter(tempFile, StandardCharsets.UTF_8)) {
            out.write(fileContent);
        } catch (IOException e) {
            throw new TempFileException("Unable to copy content: " + fileContent + " to temp file");
        }
        return tempFile;
    }

    private SpectralIssue[] parseSpectralResponse(String json) throws JsonSyntaxException {
        return new Gson().fromJson(json, SpectralIssue[].class);
    }
}
