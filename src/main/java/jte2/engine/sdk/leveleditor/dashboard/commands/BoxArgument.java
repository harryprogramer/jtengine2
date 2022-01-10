package jte2.engine.sdk.leveleditor.dashboard.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import jte2.engine.sdk.leveleditor.Editor;
import jte2.engine.sdk.leveleditor.dashboard.ConsoleSession;
import jte2.engine.sdk.leveleditor.dashboard.SpatialService;
import jte2.engine.sdk.leveleditor.dashboard.args.Argument;
import jte2.engine.sdk.leveleditor.dashboard.args.parser.LineArgument;
import jte2.engine.twilight.material.Material;
import jte2.engine.twilight.spatials.Box;
import jte2.engine.twilight.material.Colour;

public class BoxArgument extends Argument {
    private static final Logger logger = LogManager.getLogger(BoxArgument.class);
    private final Editor editor;
    private final SpatialService service;

    public BoxArgument(Editor editor, SpatialService spatialService){
        this.editor = editor;
        this.service = spatialService;
    }

    @Override
    public void handle(@NotNull LineArgument arg, ConsoleSession session) {
        if (arg.getArgumentsSize() < 3) {
            errorEcho("Syntax error, excepted 3 arguments after function", session);
        } else {

            String[] args = arg.getArguments();
            Vector3f vector3f;
            try {
                vector3f = new Vector3f(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
            }catch (Exception e){
                errorEcho("Syntax error, internal exception: " + e.getMessage(), session);
                return;
            }
            Box box;
            if(arg.getArgumentsSize() == 6){
                int x, y, z;
                try{
                    x = Integer.parseInt(args[3]);
                    y = Integer.parseInt(args[4]);
                    z = Integer.parseInt(args[5]);
                }catch (Exception e){
                    errorEcho("Syntax error, parse failed: " + e.getMessage(), session);
                    return;
                }
                box = new Box(x, y, z);
            }else {
                box = new Box(1, 1, 1);
            }
            Material material = new Material();
            material.setColour(Colour.WHITE);
            box.setMaterial(material);
            box.setTransformation(vector3f);
            int id = service.addSpatial(box);
            if(editor.getScene() == null){
                session.writeLine("Scene was not created. Use newmdl to create new model or newterrain to create terrain scene.", Colour.YELLOW);
                return;
            }
            editor.getScene().attachChild(box);
            Vector3f pos = box.getTransformation();
            session.writeLine(String.format("Box created with id [%d] at X: %.2f, Y: %.2f, Z: %.2f", id, pos.x, pos.y, pos.z), Colour.GREEN);
        }
    }

    private void errorEcho(String text, @NotNull ConsoleSession session){
        logger.error(text);
        session.writeLine(text, Colour.RED);
    }
}
