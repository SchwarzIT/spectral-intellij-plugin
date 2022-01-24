package com.schwarzit.spectralIntellijPlugin.exceptions;

public class TempFileException extends Exception {
    public TempFileException(String message) {
        super(message);
    }

    public TempFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
