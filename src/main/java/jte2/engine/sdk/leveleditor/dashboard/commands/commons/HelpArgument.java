package jte2.engine.sdk.leveleditor.dashboard.commands.commons;

import jte2.engine.sdk.leveleditor.dashboard.ConsoleSession;
import jte2.engine.sdk.leveleditor.dashboard.args.Argument;
import jte2.engine.sdk.leveleditor.dashboard.args.handler.ArgsHandlerService;
import jte2.engine.sdk.leveleditor.dashboard.args.parser.LineArgument;
import jte2.engine.twilight.audio.jmf.SoundManager;
import jte2.engine.twilight.material.Colour;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class HelpArgument extends Argument {
    private final ArgsHandlerService service;

    public HelpArgument(ArgsHandlerService service){
        this.service = service;
    }

    @Override
    public void handle(LineArgument arg, @NotNull ConsoleSession session) {
        session.writeLine("Fetching information about handles...");
        int descAvailable = 0;
        for(Argument argument : service.getHandles().values()){
            if(argument.getHelpDescription() != null)
                descAvailable++;
        }
        session.writeLine(String.format("Found %s available commands", service.getHandles().size()), Colour.GREEN);
        session.writeLine(String.format("There is %d out of %d found has a description available", descAvailable,
                service.getHandles().size()), Colour.GREEN);
        for (Map.Entry<String, Argument> entry : service.getHandles().entrySet()) {
            Argument handle = entry.getValue();
            String argument = entry.getKey();
            session.writeLine(String.format("%-10s - %s", argument, handle.getHelpDescription() == null ?
                    "no description found" : handle.getHelpDescription()), Colour.RED);
        }
    }

    @Override
    public String getHelpDescription() {
        return null;
    }
}
