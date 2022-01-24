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
    private final NotificationHandler notificationHandler;
    protected String EXECUTABLE_PATH;
    protected Path STORAGE_PATH;

    private StorageManager() throws SpectralException {
        this.notificationHandler = NotificationHandler.getInstance();
        initialize();
    }

    public StorageManager(NotificationHandler notificationHandler) throws SpectralException {
        this.notificationHandler = notificationHandler;
        initialize();
    }

    private void initialize() throws SpectralException {
        String os = System.getProperty("os.name");
        String spectralExecutablesPath = "spectralExecutables/";

        this.EXECUTABLE_PATH = calculateExecutablePath(os, spectralExecutablesPath);

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

    private String calculateExecutablePath(String os, String spectralExecutablesPath) throws SpectralException {
        String lowerCaseOS = os.toLowerCase();

        if (lowerCaseOS.contains("win")) return spectralExecutablesPath + "spectral-windows.exe";
        else if (lowerCaseOS.contains("mac")) return spectralExecutablesPath + "spectral-macos";
        else if (lowerCaseOS.contains("nix") || lowerCaseOS.contains("nux") || lowerCaseOS.contains("aix"))
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
                notificationHandler.showNotification("Ruleset download failed", content, NotificationType.WARNING);
            } else {
                notificationHandler.showNotification("Ruleset download failed", content, NotificationType.ERROR);
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
