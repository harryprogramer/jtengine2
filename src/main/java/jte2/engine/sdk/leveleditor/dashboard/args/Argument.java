package jte2.engine.sdk.leveleditor.dashboard.args;

import jte2.engine.sdk.leveleditor.dashboard.ConsoleSession;
import jte2.engine.sdk.leveleditor.dashboard.args.parser.LineArgument;

public abstract class Argument {
    private boolean isAsync = false;

    public void setAsync(boolean async){
        this.isAsync = async;
    }

    public boolean isAsync(){
        return isAsync;
    }

    public abstract void handle(LineArgument arg, ConsoleSession session);

}
