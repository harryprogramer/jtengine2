package jte2.engine.sdk.leveleditor.dashboard.commands.commons;

import jte2.engine.sdk.leveleditor.dashboard.ConsoleSession;
import jte2.engine.sdk.leveleditor.dashboard.args.Argument;
import jte2.engine.sdk.leveleditor.dashboard.args.parser.LineArgument;

public class ExitArgument extends Argument {
    @Override
    public void handle(LineArgument arg, ConsoleSession session) {
        System.exit(-1);
    }

    @Override
    public String getHelpDescription() {
        return null;
    }
}
