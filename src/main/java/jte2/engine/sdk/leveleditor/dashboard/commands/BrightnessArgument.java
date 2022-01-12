package jte2.engine.sdk.leveleditor.dashboard.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import jte2.engine.sdk.leveleditor.Editor;
import jte2.engine.sdk.leveleditor.dashboard.ConsoleSession;
import jte2.engine.sdk.leveleditor.dashboard.args.Argument;
import jte2.engine.sdk.leveleditor.dashboard.args.parser.LineArgument;
import jte2.engine.twilight.material.Colour;

public class BrightnessArgument extends Argument {
    private final static Logger logger = LogManager.getLogger(BrightnessArgument.class);
    private final Editor editor;

    public BrightnessArgument(Editor editor){
        this.editor = editor;
    }

    @Override
    public void handle(LineArgument arg, ConsoleSession session) {
        float brightness;
        if(arg.getArgumentsSize() != 1){
            echoText("Syntax error. Excepted 1 argument (brightness)", session);
            return;
        }
        try{
            brightness = Float.parseFloat(arg.getArguments()[0]);
        }catch (NumberFormatException e){
            echoText("Syntax error. Excepted 1 argument (float).", session);
            return;
        }

        editor.viewPort.getLight(0).getAttenuation().set(brightness);
        session.writeLine("Light brightness change to [" + brightness + "]", Colour.GREEN);
        logger.info("Scene light brightness change to [" + brightness + "]");
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
