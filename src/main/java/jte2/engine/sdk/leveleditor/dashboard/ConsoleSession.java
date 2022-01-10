package jte2.engine.sdk.leveleditor.dashboard;

import jte2.engine.twilight.material.Colour;

public interface ConsoleSession {
    String readLine();

    String readLine(long timeout);

    void writeLine(String text);

    void writeLine(String text, Colour colour);

    void disconnect();
}
