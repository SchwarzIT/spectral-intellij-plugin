package com.schwarzit.spectralIntellijPlugin.exceptions;

import java.net.URL;

public class DownloadFailedException extends Exception {
    private final URL url;

    public DownloadFailedException(URL url) {
        super("Download of file at url: " + url + " failed");
        this.url = url;
    }

    public URL getURL() {
        return url;
    }
}
