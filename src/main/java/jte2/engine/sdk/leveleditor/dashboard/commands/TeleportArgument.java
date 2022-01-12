package jte2.engine.sdk.leveleditor.dashboard.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import jte2.engine.sdk.leveleditor.Editor;
import jte2.engine.sdk.leveleditor.dashboard.ConsoleSession;
import jte2.engine.sdk.leveleditor.dashboard.args.Argument;
import jte2.engine.sdk.leveleditor.dashboard.args.parser.LineArgument;
import jte2.engine.twilight.material.Colour;

public class TeleportArgument extends Argument {
    private final static Logger logger = LogManager.getLogger(TeleportArgument.class);
    private final Editor editor;

    public TeleportArgument(Editor editor){
        setAsync(true);
        this.editor = editor;
    }

    @Override
    public void handle(@NotNull LineArgument arg, ConsoleSession session) {
        if(arg.getArgumentsSize() != 3){
            echoText("Syntax error, excepted 3 arguments", session);
            return;
        }

        String[] args = arg.getArguments();
        try {
            int x = Integer.parseInt(args[0]);
            int y = Integer.parseInt(args[1]);
            int z = Integer.parseInt(args[2]);
            editor.getViewPort().getCamera().setTransformation(x, y, z);
            session.writeLine("Camera editor moved to " + String.format("%d, %d, %d", x, y, z), Colour.GREEN);
        }catch (Exception e){
            echoText("Syntax error, parse failed: " + e.getMessage(), session);
        }
    }

    @Override
    public String getHelpDescription() {
        return null;
    }

    private void echoText(String text, @NotNull ConsoleSession session){
        logger.error(text);
        session.writeLine(text, Colour.RED);
    }
}
