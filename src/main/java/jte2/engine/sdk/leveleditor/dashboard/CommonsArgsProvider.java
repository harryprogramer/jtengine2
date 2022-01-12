package jte2.engine.sdk.leveleditor.dashboard;

import jte2.engine.sdk.leveleditor.dashboard.args.Argument;
import jte2.engine.sdk.leveleditor.dashboard.commands.commons.HelpArgument;
import org.jetbrains.annotations.NotNull;
import jte2.engine.sdk.leveleditor.dashboard.commands.commons.ExitArgument;

import java.util.Map;

public class CommonsArgsProvider {
    protected static void registerCommons(@NotNull AWTConsole dashboard){
        dashboard.registerArg("exit", new ExitArgument());
        dashboard.registerArg("help", new HelpArgument(dashboard.getArgsHandlerService()));
    }
}
