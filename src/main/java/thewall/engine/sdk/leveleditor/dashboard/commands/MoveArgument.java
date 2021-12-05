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

public class MoveArgument extends Argument {
    private final static Logger logger = LogManager.getLogger(MoveArgument.class);
    private final SpatialService service;
    private final Editor editor;

    public MoveArgument(Editor editor, SpatialService spatialService){
        this.service = spatialService;
        this.editor = editor;
    }

    @Override
    public void handle(LineArgument arg, DashboardSession session) {
        String[] args = arg.getArguments();
        int spatialID = Integer.parseInt(args[0]);
        Spatial spatial = service.getSpatial(spatialID);
        if(spatial == null){
            echo("Unknown spatial ID: [" + spatialID + "]", session);
            return;
        }
        switch (args[1]){
            case "x" -> editor.moveSpatialMouseX(spatial);

            case "y" -> {

            }

            case "z" -> {

            }

            default -> {
                echo("Syntax error, unknown vector position at arg [1]", session);
            }
        }
    }

    private void echo(String text, DashboardSession session){
        logger.warn(text);
        session.writeLine(text, Colour.RED);
    }
}
