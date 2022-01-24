package com.schwarzit.spectralIntellijPlugin;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.extensions.PluginId;
import com.schwarzit.spectralIntellijPlugin.config.Config;
import com.schwarzit.spectralIntellijPlugin.exceptions.DownloadFailedException;
import com.schwarzit.spectralIntellijPlugin.exceptions.SpectralException;
import com.schwarzit.spectralIntellijPlugin.util.FileDownloader;
import com.schwarzit.spectralIntellijPlugin.util.NotificationHandler;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;

public class StorageManager {
    private static StorageManager instance;
    protected String EXECUTABLE_PATH;
    protected final Path STORAGE_PATH;

    private StorageManager() throws SpectralException {
        String os = System.getProperty("os.name").toLowerCase();
        String spectralExecutablesPath = "spectralExecutables/";

        this.EXECUTABLE_PATH = calculateStoragePath(os, spectralExecutablesPath);

        try {
            this.STORAGE_PATH = calculateStoragePath();
        } catch (NullPointerException e) {
            throw new SpectralException("Failed to get the installation path of the plugin", e);
        }
    }

    private Path calculateStoragePath() throws NullPointerException {
        PluginId pluginId = PluginId.getId("com.schwarzit.spectral-intellij-plugin");
        IdeaPluginDescriptor plugin = PluginManagerCore.getPlugin(pluginId);
        return Objects.requireNonNull(plugin).getPluginPath().toAbsolutePath();
    }

    private String calculateStoragePath(String os, String spectralExecutablesPath) throws SpectralException {
        if (os.contains("win")) return spectralExecutablesPath + "spectral-windows.exe";
        else if (os.contains("mac")) return spectralExecutablesPath + "spectral-macos";
        else if (os.contains("nix") || os.contains("nux") || os.contains("aix"))
            return spectralExecutablesPath + "spectral-linux";

        throw new SpectralException("Unable to identify matching binary for operating system: " + os);
    }

    public Path getStoragePath() {
        return STORAGE_PATH;
    }

    public void installSpectralBinary() throws SpectralException {
        File executableFile = getSpectralExecutable();
        InputStream executableStream = StorageManager.class.getResourceAsStream("/" + getExecutablePath());

        try {
            FileUtils.copyInputStreamToFile(Objects.requireNonNull(executableStream), executableFile);
            boolean setExecutable = executableFile.setExecutable(true);
            if (!setExecutable)
                throw new SpectralException("Unable to make Spectral binary executable: <a href=\"" + executableFile.getAbsolutePath() + "\">" + executableFile.getAbsolutePath() + "</a>");
        } catch (IOException | NullPointerException e) {
            throw new SpectralException("Unable to unpack spectral binary to: " + "<a href= \"" + executableFile.getAbsolutePath() + "\">" + executableFile.getAbsolutePath() + "</a>");
        }
    }

    public File getSpectralExecutable() {
        return STORAGE_PATH.resolve(EXECUTABLE_PATH).toFile();
    }

    public String getExecutablePath() {
        return EXECUTABLE_PATH;
    }

    public Path getDefaultRulesetPath() {
        return STORAGE_PATH.resolve(Config.Instance.DEFAULT_RULESET_NAME());
    }

    public void downloadRuleset(@NotNull URL url, @NotNull Path destination) {
        try {
            FileDownloader.downloadFile(url, destination.toString());
        } catch (DownloadFailedException e) {
            boolean defaultRulesetExists = getDefaultRulesetPath().toFile().exists();
            String content = "<p>Unable to download default ruleset from:</p><a href=\"" + e.getURL() + "\">" + e.getURL() + "</a>";
            if (defaultRulesetExists) {
                content += "<br>Using previously downloaded version, the Rules might be out of date.";
                NotificationHandler.showNotification("Ruleset download failed", content, NotificationType.WARNING);
            } else {
                NotificationHandler.showNotification("Ruleset download failed", content, NotificationType.ERROR);
                throw new RuntimeException("Initial ruleset download failed", e);
            }
        }
    }

    public static StorageManager getInstance() throws SpectralException {
        if (instance == null) {
            instance = new StorageManager();
        }

        return instance;
    }
}
