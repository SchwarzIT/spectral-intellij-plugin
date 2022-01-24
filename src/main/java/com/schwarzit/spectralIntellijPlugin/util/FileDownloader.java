package com.schwarzit.spectralIntellijPlugin.util;

import com.schwarzit.spectralIntellijPlugin.exceptions.DownloadFailedException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class FileDownloader {
    public static void downloadFile(URL url, String destination) throws DownloadFailedException {
        try (InputStream inputStream = url.openStream()) {
            ReadableByteChannel rbc = Channels.newChannel(inputStream);
            FileOutputStream fos = new FileOutputStream(destination);
            fos.getChannel().transferFrom(rbc, 0, Integer.MAX_VALUE);
        } catch (IOException e) {
            throw new DownloadFailedException(url);
        }
    }
}
