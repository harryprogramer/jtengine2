package thewall.engine.sdk.leveleditor.dashboard;

import org.jetbrains.annotations.NotNull;
import thewall.engine.sdk.leveleditor.dashboard.commands.commons.ExitArgument;

public class CommonsArgsProvider {
    protected static void registerCommons(@NotNull EditorDashboard dashboard){
        dashboard.registerArg("exit", new ExitArgument());
    }
}
