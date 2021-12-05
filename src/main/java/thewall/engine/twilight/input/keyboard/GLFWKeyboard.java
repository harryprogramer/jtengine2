package thewall.engine.twilight.input.keyboard;

import thewall.engine.twilight.system.lwjgl.LegacyLwjglContext;

import static org.lwjgl.glfw.GLFW.*;

public class GLFWKeyboard implements Keyboard{
    private final LegacyLwjglContext context;

    public GLFWKeyboard(LegacyLwjglContext context){
        this.context = context;
    }

    @Override
    public boolean isKeyPressed(KeyboardKeys key) {
        return glfwGetKeyScancode(KeyboardKeys.enumToKey(key)) == GLFW_PRESS;
        //return glfwGetKey(context.getWindowHandle(), KeyboardKeys.enumToKey(key)) == GLFW_PRESS;
    }

    @Override
    public boolean isKeyReleased(KeyboardKeys key) {
        return glfwGetKeyScancode(KeyboardKeys.enumToKey(key)) == GLFW_RELEASE;
        //return glfwGetKey(context.getWindowHandle(), KeyboardKeys.enumToKey(key)) == GLFW_RELEASE;
    }
}
