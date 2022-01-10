package jte2.game.thewall.input;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;
import jte2.engine.twilight.input.keyboard.KeyboardKeys;
import jte2.engine.twilight.input.keyboard.KeyListener;
import jte2.engine.twilight.input.mouse.Mouse;
import jte2.game.thewall.Game;

public class KeyboardCallback implements KeyListener {
    @Override
    public void invoke(@NotNull KeyboardKeys key, int scancode, int action, int mods) {
        switch (key){
            case ESCAPE_KEY -> {
                Game.getGame().stop();
            }

            case F5_KEY -> {
                if(action == GLFW.GLFW_RELEASE) {
                    if (!Game.getGame().isImmediateGUIHidden()) {
                        Game.getGame().showImmediateGUI();
                    } else {
                        Game.getGame().hideImmediateGUI();
                    }
                }
            }

            case F3_KEY -> {
                if(action == GLFW.GLFW_RELEASE) {
                    Mouse mouse = Game.getGame().input().getMouse();
                    if (mouse.isCursorDisabled()) {
                        mouse.showCursor();
                    } else {
                        mouse.disableCursor();
                    }
                }
            }
        }
    }
}
