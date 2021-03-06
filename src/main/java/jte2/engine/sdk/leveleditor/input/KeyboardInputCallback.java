package jte2.engine.sdk.leveleditor.input;

import jte2.engine.sdk.leveleditor.Editor;
import jte2.engine.twilight.input.keyboard.KeyboardKeys;
import jte2.engine.twilight.input.keyboard.KeyListener;
import jte2.engine.twilight.runtime.AbstractRuntime;
import jte2.engine.twilight.runtime.TwilightRuntimeService;

import static org.lwjgl.glfw.GLFW.*;

public class KeyboardInputCallback implements KeyListener {
    private final Editor editor;

    public KeyboardInputCallback(Editor editor){
        this.editor = editor;
    }

    @Override
    public void invoke(KeyboardKeys key, int scancode, int action, int mods) {
        if(key == KeyboardKeys.F3_KEY && action == GLFW_RELEASE){
            if(editor.getInput().getMouse().isCursorDisabled()){
                editor.getInput().getMouse().showCursor();
                editor.setCamera(false);
            }else {
                editor.getInput().getMouse().setCursorPosition(0, 0);
                editor.getInput().getMouse().disableCursor();
                editor.setCamera(true);
            }
        }

        if(key == KeyboardKeys.ESCAPE_KEY && action == GLFW_RELEASE){
            AbstractRuntime<?> runtime = TwilightRuntimeService.getActiveRuntimes().get(this);
            if(runtime != null){
                runtime.forceStop();
            }else{
                System.exit(-1);
            }
        }

    }
}
