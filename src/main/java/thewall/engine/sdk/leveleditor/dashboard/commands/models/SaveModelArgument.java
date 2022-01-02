package thewall.engine.sdk.leveleditor.dashboard.commands.models;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import thewall.engine.sdk.leveleditor.Editor;
import thewall.engine.sdk.leveleditor.dashboard.ConsoleSession;
import thewall.engine.sdk.leveleditor.dashboard.args.Argument;
import thewall.engine.sdk.leveleditor.dashboard.args.parser.LineArgument;
import thewall.engine.twilight.material.Colour;
import thewall.engine.twilight.spatials.Spatial;
import thewall.engine.twilight.viewport.Node;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class SaveModelArgument extends Argument {
    private final static Logger logger = LogManager.getLogger(SaveModelArgument.class);
    private final Editor editor;

    public SaveModelArgument(Editor editor){
        this.editor = editor;
    }

    @Override
    public void handle(LineArgument arg, ConsoleSession session) {
        JSONObject object = new JSONObject();
        if(editor.getScene() == null){
            session.writeLine("Scene was not created. Use newmdl to create new model or newterrain to create terrain scene.", Colour.YELLOW);
            return;
        }
        Node scene = editor.getScene();
        JSONArray array = new JSONArray();
        for(Spatial spatial : scene.getChildren()){
            JSONObject jsonSpatial = new JSONObject();
            jsonSpatial.put("name", spatial.getName());
            array.put(jsonSpatial);
        }

        object.put("node", array);
        try(PrintWriter out = new PrintWriter(editor.getScene().getName() + ".jtemdl")){
            out.println(object);
            editor.setScene(null);
        } catch (FileNotFoundException e) {
            logger.error("Cannot serialize model [" + editor.getScene().getName() + "]", e);
            session.writeLine("Cannot save model, " + e.getMessage(), Colour.RED);
        }
    }
}
