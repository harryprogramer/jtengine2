package jte2.engine.sdk.leveleditor.dashboard;

import jte2.engine.sdk.leveleditor.dashboard.args.Argument;

public interface EditorDashboard {
    void startDashboard();

    void disableDashboard();

    void registerArg(String arg, Argument handler);

    void unregisterArg(String arg);
}
