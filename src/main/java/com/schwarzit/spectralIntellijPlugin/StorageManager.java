package com.schwarzit.spectralIntellijPlugin;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.openapi.extensions.PluginId;
import com.schwarzit.spectralIntellijPlugin.config.Config;
import com.schwarzit.spectralIntellijPlugin.exceptions.SpectralException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Objects;

public class StorageManager {
    private static StorageManager instance;
    protected Path EXECUTABLE_PATH;
    protected Path STORAGE_PATH;

    private StorageManager() throws SpectralException {
        initialize();
    }

    private void initialize() throws SpectralException {
        String os = System.getProperty("os.name");

        try {
            this.STORAGE_PATH = calculateStoragePath();
            Path spectralExecutablesPath = getStoragePath().resolve("spectralExecutables");
            this.EXECUTABLE_PATH = spectralExecutablesPath.resolve(calculateExecutableName(os));
        } catch (NullPointerException e) {
            throw new SpectralException("Failed to get the installation path of the plugin", e);
        }
    }

    private Path calculateStoragePath() throws NullPointerException {
        PluginId pluginId = PluginId.getId("com.schwarzit.spectral-intellij-plugin");
        IdeaPluginDescriptor plugin = PluginManagerCore.getPlugin(pluginId);
        return Objects.requireNonNull(plugin).getPluginPath().toAbsolutePath();
    }

    private String calculateExecutableName(String os) throws SpectralException {
        String lowerCaseOS = os.toLowerCase();

        if (lowerCaseOS.contains("win")) return "spectral-windows.exe";
        else if (lowerCaseOS.contains("mac")) return "spectral-macos";
        else if (lowerCaseOS.contains("nix") || lowerCaseOS.contains("nux") || lowerCaseOS.contains("aix"))
            return "spectral-linux";

        throw new SpectralException("Unable to identify matching binary for operating system: " + os);
    }

    public Path getStoragePath() {
        return STORAGE_PATH;
    }

    public void installSpectralBinary() throws SpectralException {
        File executableFile = getSpectralExecutable();
        if (executableFile.exists()) return;

        InputStream executableStream = StorageManager.class.getResourceAsStream("/spectralExecutables/" + calculateExecutableName(System.getProperty("os.name")));

        try {
            FileUtils.copyInputStreamToFile(Objects.requireNonNull(executableStream), executableFile);
            boolean setExecutable = executableFile.setExecutable(true);
            if (!setExecutable)
                throw new SpectralException("Unable to make Spectral binary executable: <a href=\"" + executableFile.getAbsolutePath() + "\">" + executableFile.getAbsolutePath() + "</a>");
        } catch (IOException | NullPointerException e) {
            throw new SpectralException("Unable to unpack spectral binary to: " + "<a href= \"" + executableFile.getAbsolutePath() + "\">" + executableFile.getAbsolutePath() + "</a>");
        }
    }

    public void installDefaultRuleset() throws SpectralException {
        File defaultRuleset = getDefaultRulesetPath().toFile();
        if (defaultRuleset.exists()) return;

        InputStream resourceAsStream = StorageManager.class.getResourceAsStream("/" + Config.Instance.DEFAULT_RULESET_NAME());

        try {
            FileUtils.copyInputStreamToFile(Objects.requireNonNull(resourceAsStream), defaultRuleset);
        } catch (IOException e) {
            throw new SpectralException("Unable to unpack default ruleset to: " + "<a href= \"" + defaultRuleset.getAbsolutePath() + "\">" + defaultRuleset.getAbsolutePath() + "</a>");
        }
    }

    public File getSpectralExecutable() {
        return EXECUTABLE_PATH.toFile();
    }

    public Path getDefaultRulesetPath() {
        return STORAGE_PATH.resolve(Config.Instance.DEFAULT_RULESET_NAME());
    }

    public static StorageManager getInstance() throws SpectralException {
        if (instance == null) {
            instance = new StorageManager();
        }

        return instance;
    }
}
