package thewall.engine.sdk.leveleditor.dashboard.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector3f;
import thewall.engine.sdk.leveleditor.Editor;
import thewall.engine.sdk.leveleditor.dashboard.DashboardSession;
import thewall.engine.sdk.leveleditor.dashboard.SpatialService;
import thewall.engine.sdk.leveleditor.dashboard.args.Argument;
import thewall.engine.sdk.leveleditor.dashboard.args.parser.LineArgument;
import thewall.engine.twilight.entity.Box;
import thewall.engine.twilight.utils.Colour;

import java.util.Arrays;

public class BoxArgument extends Argument {
    private static final Logger logger = LogManager.getLogger(BoxArgument.class);
    private final Editor editor;
    private final SpatialService service;

    public BoxArgument(Editor editor, SpatialService spatialService){
        this.editor = editor;
        this.service = spatialService;
    }

    @Override
    public void handle(LineArgument arg, DashboardSession session) {
        if (arg.getArgumentsSize() != 3) {
            session.writeLine("Syntax error, excepted 3 arguments after function", Colour.RED);
        } else {
            String[] args = arg.getArguments();
            Vector3f vector3f;
            try {
                vector3f = new Vector3f(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
            }catch (Exception e){
                session.writeLine("Syntax error, internal exception: " + e.getMessage() );
                return;
            }
            Box box = new Box(new Vector3f(0, 0, 0), 1, 1, 1);
            box.setTransformation(vector3f);
            int id = service.addSpatial(box);
            editor.rootNode.attachChild(box);
            session.writeLine("Box created with id [" + id + "]", Colour.GREEN);
        }
    }
}
