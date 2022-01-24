package com.schwarzit.spectralIntellijPlugin.util;

import com.schwarzit.spectralIntellijPlugin.exceptions.DownloadFailedException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileDownloaderTest {
    private Path tempFile;
    private URL url;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = Files.createTempFile(null, ".html");
        url = new URL("https://www.google.com/index.html");
    }

    @Test
    void testDownloadFile() throws DownloadFailedException {
        assertTrue(tempFile.toFile().exists());
        assertEquals(0, tempFile.toFile().length());
        FileDownloader.downloadFile(url, tempFile.toAbsolutePath().toString());
        assertTrue(tempFile.toFile().length() > 0);
    }

    @Test
    void testDownloadFileWithException() throws MalformedURLException {
        URL url = new URL("https://www.invalid-url-i-hope-this-will-never-exist.com/index.html");
        assertThrows(DownloadFailedException.class, () -> FileDownloader.downloadFile(url, tempFile.toAbsolutePath().toString()));
    }

    @AfterEach
    void tearDown() {
        //noinspection ResultOfMethodCallIgnored
        tempFile.toFile().delete();
    }
}