package jte2.engine.sdk.leveleditor.dashboard.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import jte2.engine.sdk.leveleditor.dashboard.ConsoleSession;
import jte2.engine.sdk.leveleditor.dashboard.SpatialService;
import jte2.engine.sdk.leveleditor.dashboard.args.Argument;
import jte2.engine.sdk.leveleditor.dashboard.args.parser.LineArgument;
import jte2.engine.twilight.material.Colour;
import jte2.engine.twilight.spatials.Spatial;

public class ScaleArgument extends Argument {
    private final static Logger logger = LogManager.getLogger(ScaleArgument.class);
    private final SpatialService spatialService;

    public ScaleArgument(SpatialService service){
        this.spatialService = service;
    }

    @Override
    public void handle(LineArgument arg, ConsoleSession session) {
        if(arg.getArgumentsSize() != 2){
            echoText("Syntax error, Excepted 2 arguments (id, scale)", session);
            return;
        }
        int id, scale;
        try{
            id = Integer.parseInt(arg.getArguments()[0]);
            scale = Integer.parseInt(arg.getArguments()[1]);
        }catch (Exception e){
            echoText("Syntax error, Excepted 2 data types (int, int), " + e.getMessage(), session);
            return;
        }

        Spatial spatial = spatialService.getSpatial(id);
        if(spatial == null){
            echoText("Cannot find spatial of id [" + id + "]", session);
            return;
        }

        spatial.setScale(scale);
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
