package thewall.engine.twilight.input.keyboard;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thewall.engine.twilight.system.lwjgl.LegacyLwjglContext;

import static org.lwjgl.glfw.GLFW.*;
import static thewall.engine.twilight.system.lwjgl.GLFWInputUtils.enumToKey;
import static thewall.engine.twilight.system.lwjgl.GLFWInputUtils.keyToEnum;

public class GLFWKeyboard implements Keyboard{
    private final static Logger logger = LogManager.getLogger(GLFWKeyboard.class);
    private final LegacyLwjglContext context;

    public GLFWKeyboard(LegacyLwjglContext context){
        this.context = context;
    }

    @Override
    public boolean isKeyPressed(KeyboardKeys key) {
        //return glfwGetKeyScancode(KeyboardKeys.enumToKey(key)) == GLFW_PRESS;
        int state = glfwGetKey(context.getWindowHandle(), enumToKey(key));
        if(state == GLFW_KEY_UNKNOWN){
            logger.warn("Unknown key state for [" + key.name() + "]");
        }
        return state == GLFW_PRESS;
    }

    @Override
    public boolean isKeyReleased(KeyboardKeys key) {
        //return glfwGetKeyScancode(KeyboardKeys.enumToKey(key)) == GLFW_RELEASE;
        int state = glfwGetKey(context.getWindowHandle(), enumToKey(key));
        if(state == GLFW_KEY_UNKNOWN){
            logger.warn("Unknown key state for [" + key.name() + "]");
        }
        return state == GLFW_RELEASE;
    }

    @Override
    public void setKeyboardCallback(KeyListener callback) {
        glfwSetKeyCallback(context.getWindow(), (window, key, scancode, action, mods) -> {
            callback.invoke(keyToEnum(key), scancode, action, mods); // TODO: implement own scancodes
        });
    }
}
