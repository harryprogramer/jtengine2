package thewall.engine.twilight.input.mouse;


import org.jetbrains.annotations.NotNull;
import org.lwjgl.BufferUtils;
import thewall.engine.twilight.system.context.opengl.lwjgl.LegacyLwjglContext;

import static org.lwjgl.glfw.GLFW.*;
import java.nio.DoubleBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GLFWMouse implements Mouse{
    private final Map<MouseButton, Byte> states = new ConcurrentHashMap<>();
    private final LegacyLwjglContext app;

    public GLFWMouse(@NotNull LegacyLwjglContext context){
        this.app = context;
    }

    private static class CursorPositionImpl implements CursorPosition {
       private final double posX;
       private final double posY;

       public CursorPositionImpl(double posX, double posY){
           this.posX = posX;
           this.posY = posY;
       }

        @Override
        public double getXPos() {
            return posX;
        }

        @Override
        public double getYPos() {
            return posY;
        }
    }

    @Override
    public CursorPosition getCursorPosition() {
        DoubleBuffer xPos = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer yPos = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(app.getWindowHandle(), xPos, yPos);
        return new CursorPositionImpl(xPos.get(0), yPos.get(0));
    }

    @Override
    public void setCursorPositionCallback(CursorPositionCallback callback) throws UnsupportedOperationException {
        glfwSetCursorPosCallback(app.getWindowHandle(), (window, xpos, ypos) -> callback.invoke(xpos, ypos));
    }

    @Override
    public void setCursorPosition(double posX, double posY) {
        glfwSetCursorPos(app.getWindowHandle(), posX, posY);
    }

    @Override
    public boolean mousePressed(MouseButton button) {
        return glfwGetMouseButton(app.getWindowHandle(), MouseButton.buttonToEnum(button)) == GLFW_PRESS;
    }

    @Override
    public boolean mouseReleased(MouseButton button) {
        byte state = (byte) glfwGetMouseButton(app.getWindowHandle(), MouseButton.buttonToEnum(button));

        if(state == GLFW_PRESS){
            states.put(button, state);
        }else if(state == 0 && states.get(button) != null){
            boolean statement = states.get(button) == GLFW_PRESS;
            if(statement){
                states.remove(button);
            }
            return statement;
        }
        return false;
    }


    @Override
    public void createMouseKeyCallback(TMouseCallback callback) {
        glfwSetMouseButtonCallback(app.getWindowHandle(), (window, button, action, mods) -> callback.invoke(button, action, mods));
    }

    @Override
    public void hideCursor() {
        glfwSetInputMode(app.getWindowHandle(), GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
    }

    @Override
    public void showCursor() {
        glfwSetInputMode(app.getWindowHandle(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);
    }

    @Override
    public boolean isCursorDisabled() {
        return glfwGetInputMode(app.getWindowHandle(), GLFW_CURSOR) == GLFW_CURSOR_DISABLED;
    }

    @Override
    public void disableCursor() {
        glfwSetInputMode(app.getWindowHandle(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
    }

    @Override
    public boolean isCursorHidden() {
        return glfwGetInputMode(app.getWindowHandle(), GLFW_CURSOR) == GLFW_CURSOR_HIDDEN;
    }
}
