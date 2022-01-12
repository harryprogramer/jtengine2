package jte2.engine.sdk.leveleditor.dashboard.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import jte2.engine.sdk.leveleditor.Editor;
import jte2.engine.sdk.leveleditor.dashboard.ConsoleSession;
import jte2.engine.sdk.leveleditor.dashboard.SpatialService;
import jte2.engine.sdk.leveleditor.dashboard.args.Argument;
import jte2.engine.sdk.leveleditor.dashboard.args.parser.LineArgument;
import jte2.engine.twilight.assets.AssetManager;
import jte2.engine.twilight.material.Colour;
import jte2.engine.twilight.spatials.Spatial;

public class ModelCommand extends Argument {
    private final static Logger logger = LogManager.getLogger(ModelCommand.class);
    private final SpatialService spatialService;
    private final AssetManager assetManager;
    private final Editor editor;

    public ModelCommand(Editor editor, SpatialService spatialService, AssetManager assetManager){
        this.spatialService = spatialService;
        this.assetManager = assetManager;
        this.editor = editor;
    }
    @Override
    public void handle(LineArgument arg, ConsoleSession session) {
        if(arg.getArgumentsSize() != 1){
            echoText("Syntax error, Excepted one argument (filename)", session);
            return;
        }

        if(editor.getScene() == null){
            session.writeLine("Scene was not created. Use newmdl to create new model or newterrain to create terrain scene.", Colour.YELLOW);
            return;
        }

        String filename = arg.getArguments()[0];
        Spatial spatial;

        try {
            spatial = assetManager.loadModel(filename);
        }catch (Exception e){
            echoText("Cannot load model, " + e.getMessage(), session);
            return;
        }

        int id = spatialService.addSpatial(spatial);
        editor.getScene().attachChild(spatial);
        session.writeLine("Loaded model with ID: [" + id + "]" , Colour.GREEN);
        logger.info("Loaded model [" + filename + "] to ID: [" + id + "]");
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
