package thewall.engine.sdk.leveleditor.dashboard;

import thewall.engine.sdk.leveleditor.dashboard.commands.commons.ExitArgument;

public class CommonsArgsProvider {
    protected static void registerCommons(EditorDashboard dashboard){
        dashboard.registerArg("exit", new ExitArgument());
    }
}
