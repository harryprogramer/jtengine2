package jte2.engine.sdk.leveleditor.dashboard.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import jte2.engine.sdk.leveleditor.Editor;
import jte2.engine.sdk.leveleditor.dashboard.ConsoleSession;
import jte2.engine.sdk.leveleditor.dashboard.SpatialService;
import jte2.engine.sdk.leveleditor.dashboard.args.Argument;
import jte2.engine.sdk.leveleditor.dashboard.args.parser.LineArgument;
import jte2.engine.twilight.spatials.Spatial;
import jte2.engine.twilight.material.Colour;

public class DestroyArgument extends Argument {
    private final static Logger logger = LogManager.getLogger(DestroyArgument.class);
    private final SpatialService spatialService;
    private final Editor editor;

    public DestroyArgument(Editor editor, SpatialService spatialService){
        this.spatialService = spatialService;
        this.editor = editor;
    }

    @Override
    public void handle(LineArgument arg, ConsoleSession session) {
        int id;
        if(editor.getScene() == null){
            session.writeLine("Scene was not created. Use newmdl to create new model or newterrain to create terrain scene.", Colour.YELLOW);
            return;
        }

        try {
            id = Integer.parseInt(arg.getArguments()[0]);
        }catch (Exception e){
            session.writeLine("Syntax error, internal exception: " + e.getMessage(), Colour.RED);
            logger.warn("Syntax error for [" + arg.getName() + "], internal exception: "+ e.getMessage());
            return;
        }

        Spatial spatial = spatialService.getSpatial(id);
        if(spatial == null){
            session.writeLine("Spatial doesn't exist.", Colour.RED);
            logger.warn("Unresolved spatial [" + id + "]");
            return;
        }
        try {
            editor.getScene().detachChild(spatial);
        }catch (Exception e){
            session.writeLine("This spatial does not exist on this scene", Colour.YELLOW);
            return;
        }
        spatialService.removeSpatial(id);
        session.writeLine("Spatial [" + id + "] destroyed.", Colour.GREEN);
    }

    @Override
    public String getHelpDescription() {
        return null;
    }
}
