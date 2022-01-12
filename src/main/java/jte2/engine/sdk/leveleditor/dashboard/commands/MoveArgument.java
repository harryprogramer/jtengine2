package jte2.engine.sdk.leveleditor.dashboard.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector3f;
import jte2.engine.sdk.leveleditor.Editor;
import jte2.engine.sdk.leveleditor.dashboard.ConsoleSession;
import jte2.engine.sdk.leveleditor.dashboard.SpatialService;
import jte2.engine.sdk.leveleditor.dashboard.args.Argument;
import jte2.engine.sdk.leveleditor.dashboard.args.parser.LineArgument;
import jte2.engine.twilight.spatials.Spatial;
import jte2.engine.twilight.input.Input;
import jte2.engine.twilight.input.keyboard.KeyboardKeys;
import jte2.engine.twilight.input.mouse.CursorPosition;
import jte2.engine.twilight.renderer.SyncTimer;
import jte2.engine.twilight.material.Colour;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MoveArgument extends Argument {
    private final ExecutorService executor = Executors.newFixedThreadPool(50);
    private final static Logger logger = LogManager.getLogger(MoveArgument.class);
    private final SpatialService service;
    private final Editor editor;

    public MoveArgument(Editor editor, SpatialService spatialService){
        this.service = spatialService;
        this.editor = editor;
    }

    @Override
    public void handle(LineArgument arg, ConsoleSession session) {
        String[] args = arg.getArguments();
        int spatialID = Integer.parseInt(args[0]);
        Spatial spatial = service.getSpatial(spatialID);
        if(spatial == null){
            echo("Unknown spatial ID: [" + spatialID + "]", session);
            return;
        }

        editor.getDisplay().focus();

        switch (args[1]){
            case "x" -> {
                session.writeLine("Spatial [" + spatial.getName() + "] is now selected for direction X, use mouse to move it. Press TAB to stop", Colour.GREEN);
                executor.submit(() -> moveSpatialMouseX(spatial, session));
            }

            case "y" -> {

            }

            case "z" -> {

            }

            default -> echo("Syntax error, unknown vector position at arg [1]", session);
        }
    }

    @Override
    public String getHelpDescription() {
        return null;
    }

    private static float moveSensitivity = 0.01f;

    public void moveSpatialMouseX(Spatial spatial, ConsoleSession session){
        SyncTimer syncTimer = new SyncTimer(SyncTimer.LWJGL_GLFW);
        Input input = editor.getInput();
        editor.setCamera(false);
        CursorPosition calibrateValue = input.getMouse().getCursorPosition();
        double calibrateX = calibrateValue.getXPos();
        Vector3f spatialVector = spatial.getTransformation();
        input.getMouse().disableCursor();
        while (!input.getKeyboard().isKeyPressed(KeyboardKeys.TAB_KEY)) {
            if(input.getKeyboard().isKeyPressed(KeyboardKeys.F3_KEY)){
                editor.setCamera(true);
                while (!input.getKeyboard().isKeyPressed(KeyboardKeys.F3_KEY)) {
                    try {
                        syncTimer.sync(editor.getFrameLimit());
                    } catch (Exception e) {
                        logger.warn("Error while moving object", e);
                    }
                }
            }
            CursorPosition position = input.getMouse().getCursorPosition();
            double x = position.getXPos();
            spatial.setTransformation((float) (calibrateX - x) * moveSensitivity, spatialVector.y, spatialVector.z);
            try {
                syncTimer.sync(editor.getFPS());
            } catch (Exception e) {
                logger.warn("Error while moving object", e);
                break;
            }
        }
        session.writeLine("Spatial [" + spatial.getName() + "] is deselected for direction X.", Colour.GREEN);
        input.getMouse().showCursor();
        editor.setCamera(true);
    }

    private void echo(String text, ConsoleSession session){
        logger.warn(text);
        session.writeLine(text, Colour.RED);
    }
}
