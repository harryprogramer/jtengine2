package thewall.engine.sdk.leveleditor.dashboard;

import thewall.engine.twilight.utils.Colour;

public interface DashboardSession {
    String readLine();

    String readLine(long timeout);

    void writeLine(String text);

    void writeLine(String text, Colour colour);

    void disconnect();
}
