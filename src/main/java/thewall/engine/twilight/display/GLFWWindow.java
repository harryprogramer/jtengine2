package thewall.engine.twilight.display;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.BufferUtils;
import thewall.engine.twilight.errors.NotImplementedException;

import java.awt.image.BufferedImage;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static org.lwjgl.glfw.GLFW.*;

public class GLFWWindow extends GLFWDisplayManager implements Window {
    private final static short MAX_CALLBACKS = 10;

    private final AtomicReference<String> windowName = new AtomicReference<>(null);
    private final AtomicReference<Vector2i> windowSizeLimit = new AtomicReference<>(null);

    private final List<DisplayResizeCallback> resizeCallbacks = new ArrayList<>(10);

    public GLFWWindow() {
        super();
    }

    @Override
    public String getWindowName() {
        return windowName.get() == null ? null : windowName.get();
    }

    @Override
    public void setWindowName(String name) {
        glfwSetWindowTitle(getWindow(), name);
        windowName.set(name);
    }

    @Override
    public void showWindow() {
        glfwShowWindow(getWindow());
    }

    @Override
    public void hideWindow() {
        glfwHideWindow(getWindow());
    }

    @Override
    public void setWindowSize(int x, int y) {
        glfwSetWindowSize(getWindow(), x, y);
    }

    public void addDisplayResizeCallback(DisplayResizeCallback callback){
        if(resizeCallbacks.size() >= 10){
            throw new IndexOutOfBoundsException("Maximum registered callbacks is [" + MAX_CALLBACKS + "]");
        }
        Objects.requireNonNull(callback, "Window resize callback cannot be null.");
        resizeCallbacks.add(callback);
    }

    public void unregisterDisplayCallback(DisplayResizeCallback callback){
        resizeCallbacks.remove(callback);
    }

    public void registerCallbacks(){
        glfwSetWindowSizeCallback(getWindow(), (window, width, height) -> {
            for(DisplayResizeCallback callback : resizeCallbacks){
                callback.invoke(width, height);
            }
        });
    }

    @Override
    public Vector2i getWindowSize() {
        IntBuffer x = BufferUtils.createIntBuffer(1);
        IntBuffer y = BufferUtils.createIntBuffer(1);
        glfwGetWindowSize(getWindow(), x, y);
        return new Vector2i(x.get(0), y.get(0));
    }

    @Override
    public boolean isWindowFocus() {
        return glfwGetWindowAttrib(getWindow(), GLFW_FOCUSED) == 1;
    }

    @Override
    public void requestWindowFocus() {
        glfwRequestWindowAttention(getWindow());
    }

    @Override
    public void switchFullScreen() {
        throw NotImplementedException.NOT_IMPLEMENTED; // TODO
    }

    @Override
    public void iconifyWindow() {
        glfwIconifyWindow(getWindow());
    }

    @Override
    public void maximizeWindow() {
        glfwMaximizeWindow(getWindow());
    }

    @Override
    public void destroyWindow() {
        glfwDestroyWindow(getWindow());
    }

    @Override
    public void setWindowSizeLimit(int xMin, int yMin, int xMax, int yMax) {
        glfwSetWindowSizeLimits(getWindow(), xMin, yMin, xMax, yMin);
    }

    @Override
    public Vector2i getWindowSizeLimit() {
        return windowSizeLimit.get() == null ? new Vector2i(0, 0) : windowSizeLimit.get();
    }

    @Override
    public void setWindowPosition(int x, int y) {
        glfwSetWindowPos(getWindow(), x, y);
    }

    @Override

    public void setWindowIcon(BufferedImage bufferedImage) {
        throw NotImplementedException.NOT_IMPLEMENTED; // TODO
    }

    @Override
    public void setWindowIcon(String file) {
        throw NotImplementedException.NOT_IMPLEMENTED; // TODO
    }

    @Override
    public void sendWindowAttentionRequest() {
        glfwRequestWindowAttention(getWindow());
    }

    @Override
    public void setWindowTransparency(float transparency) {
        throw NotImplementedException.NOT_IMPLEMENTED; // TODO
    }

    @Override
    public float getWindowTransparency() {
        throw NotImplementedException.NOT_IMPLEMENTED; // TODO
    }

    @Override
    public void setWindowContentScale(float x, float y) {
        throw NotImplementedException.NOT_IMPLEMENTED; // TODO
    }

    @Override
    public Vector2f getWindowContentScale() {
        throw NotImplementedException.NOT_IMPLEMENTED; // TODO
    }
}
