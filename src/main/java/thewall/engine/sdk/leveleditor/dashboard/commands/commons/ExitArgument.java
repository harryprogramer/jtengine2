package thewall.engine.sdk.leveleditor.dashboard.commands.commons;

import thewall.engine.sdk.leveleditor.dashboard.ConsoleSession;
import thewall.engine.sdk.leveleditor.dashboard.args.Argument;
import thewall.engine.sdk.leveleditor.dashboard.args.parser.LineArgument;

public class ExitArgument extends Argument {
    @Override
    public void handle(LineArgument arg, ConsoleSession session) {
        System.exit(-1);
    }
}
