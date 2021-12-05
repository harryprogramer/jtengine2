package thewall.engine.sdk.leveleditor.dashboard.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thewall.engine.sdk.leveleditor.Editor;
import thewall.engine.sdk.leveleditor.dashboard.DashboardSession;
import thewall.engine.sdk.leveleditor.dashboard.SpatialService;
import thewall.engine.sdk.leveleditor.dashboard.args.Argument;
import thewall.engine.sdk.leveleditor.dashboard.args.parser.LineArgument;
import thewall.engine.twilight.entity.Spatial;
import thewall.engine.twilight.utils.Colour;

public class DestroyArgument extends Argument {
    private final static Logger logger = LogManager.getLogger(DestroyArgument.class);
    private final SpatialService spatialService;
    private final Editor editor;

    public DestroyArgument(Editor editor, SpatialService spatialService){
        this.spatialService = spatialService;
        this.editor = editor;
    }

    @Override
    public void handle(LineArgument arg, DashboardSession session) {
        int id;
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
        editor.rootNode.detachChild(spatial);
        spatialService.removeSpatial(id);
        session.writeLine("Spatial [" + id + "] destroyed.", Colour.GREEN);
    }
}
