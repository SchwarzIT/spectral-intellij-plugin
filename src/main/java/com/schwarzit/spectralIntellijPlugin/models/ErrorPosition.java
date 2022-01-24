package com.schwarzit.spectralIntellijPlugin.models;

import java.util.Objects;

public class ErrorPosition {
    private final int line;
    private final int character;

    public ErrorPosition(int line, int character) {
        this.line = line;
        this.character = character;
    }

    public int getLine() {
        return line;
    }

    public int getCharacter() {
        return character;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorPosition that = (ErrorPosition) o;
        return line == that.line && character == that.character;
    }

    @Override
    public int hashCode() {
        return Objects.hash(line, character);
    }

    @Override
    public String toString() {
        return "ErrorPosition{" +
                "line=" + line +
                ", character=" + character +
                '}';
    }
}
