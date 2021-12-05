package thewall.engine.sdk.leveleditor.dashboard.commands.commons;

import thewall.engine.sdk.leveleditor.dashboard.DashboardSession;
import thewall.engine.sdk.leveleditor.dashboard.args.Argument;
import thewall.engine.sdk.leveleditor.dashboard.args.parser.LineArgument;

public class ExitArgument extends Argument {
    @Override
    public void handle(LineArgument arg, DashboardSession session) {
        System.exit(-1);
    }
}
