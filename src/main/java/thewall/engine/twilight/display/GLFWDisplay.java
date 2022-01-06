package thewall.engine.twilight.display;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.BufferUtils;
import thewall.engine.twilight.Area;
import thewall.engine.twilight.errors.NotImplementedException;
import thewall.engine.twilight.utils.SafeArrayList;

import java.awt.image.BufferedImage;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static org.lwjgl.glfw.GLFW.*;

public class GLFWDisplay extends GLFWDisplayManager implements Display {
    private final static short MAX_CALLBACKS = 10;

    private final AtomicReference<String> windowName = new AtomicReference<>(null);
    private final AtomicReference<Vector2i> windowSizeLimit = new AtomicReference<>(null);

    private final List<DisplayResizeCallback> resizeCallbacks = new SafeArrayList<>(DisplayResizeCallback.class, 10);

    public GLFWDisplay() {
        super();
    }

    @Override
    public String getTitle() {
        return windowName.get() == null ? null : windowName.get();
    }

    @Override
    public void setTitle(String name) {
        glfwSetWindowTitle(getWindow(), name);
        windowName.set(name);
    }

    @Override
    public void show() {
        glfwShowWindow(getWindow());
    }

    @Override
    public void hide() {
        glfwHideWindow(getWindow());
    }

    @Override
    public void setSize(int x, int y) {
        glfwSetWindowSize(getWindow(), x, y);
    }

    @Override
    public void setSize(Area area) {

    }

    @Override
    public void createDisplay(int width, int height, String name) {
        super.createDisplay(width, height, name);
        registerCallbacks();
    }


    private void addDisplayResizeCallback(DisplayResizeCallback callback){
        if(resizeCallbacks.size() >= 10){
            throw new IndexOutOfBoundsException("Maximum registered callbacks is [" + MAX_CALLBACKS + "]");
        }
        Objects.requireNonNull(callback, "Window resize callback cannot be null.");
        resizeCallbacks.add(callback);
    }

    private void unregisterDisplayCallback(DisplayResizeCallback callback){
        resizeCallbacks.remove(callback);
    }

    public void registerCallbacks(){
        glfwSetWindowSizeCallback(getWindow(), (window, width, height) -> {
            for(DisplayResizeCallback callback : resizeCallbacks){
                callback.invoke(width, height);
            }
        });
    }

    /* deprecated
        @Override
        public Vector2i getSize() {
            IntBuffer x = BufferUtils.createIntBuffer(1);
            IntBuffer y = BufferUtils.createIntBuffer(1);
            glfwGetWindowSize(getWindow(), x, y);
            return new Vector2i(x.get(0), y.get(0));
        }
     */
    @Override
    public boolean isFocus() {
        return glfwGetWindowAttrib(getWindow(), GLFW_FOCUSED) == 1;
    }

    @Override
    public void focus() {
        glfwFocusWindow(getWindow());
    }

    @Override
    public void fullscreen() {
        throw NotImplementedException.NOT_IMPLEMENTED; // TODO
    }

    @Override
    public void iconify() {
        glfwIconifyWindow(getWindow());
    }

    @Override
    public void maximize() {
        glfwMaximizeWindow(getWindow());
    }

    @Override
    public void setResizeLimit(int xMin, int yMin, int xMax, int yMax) {
        glfwSetWindowSizeLimits(getWindow(), xMin, yMin, xMax, yMin);
    }

    @Override
    public void setMinimumSize(@NotNull Area area) {
        glfwSetWindowSizeLimits(getWindow(), area.getWidth(), area.getHeight(), GLFW_DONT_CARE, GLFW_DONT_CARE);
    }

    @Override
    public void setMaximumSize(@NotNull Area area) {
        glfwSetWindowSizeLimits(getWindow(), GLFW_DONT_CARE, GLFW_DONT_CARE, area.getWidth(), area.getHeight());
    }

    @Override
    public Vector2i getResizeLimit() {
        return windowSizeLimit.get() == null ? new Vector2i(0, 0) : windowSizeLimit.get();
    }

    @Override
    public void setLocation(@NotNull Area area) {
        setLocation(area.getWidth(), area.getHeight());
    }

    @Override
    public void setLocation(int x, int y) {
        glfwSetWindowPos(getWindow(), x, y);
    }

    @Override
    public Area getLocation() {
        IntBuffer x = BufferUtils.createIntBuffer(1);
        IntBuffer y = BufferUtils.createIntBuffer(1);
        glfwGetWindowSize(getWindow(), x, y);

        return new Area(x.get(), y.get());
    }

    @Override
    public void setPosition(int x, int y) {
        glfwSetWindowPos(getWindow(), x, y);
    }

    @Override
    public void setIcon(BufferedImage bufferedImage) {
        throw NotImplementedException.NOT_IMPLEMENTED; // TODO
    }

    @Override
    public void setIcon(String file) {
        throw NotImplementedException.NOT_IMPLEMENTED; // TODO
    }

    @Override
    public void sendAttention() {
        glfwRequestWindowAttention(getWindow());
    }

    @Override
    public void setTransparency(float transparency) {
        throw NotImplementedException.NOT_IMPLEMENTED; // TODO
    }

    @Override
    public float getTransparency() {
        throw NotImplementedException.NOT_IMPLEMENTED; // TODO
    }

    @Override
    public void sizeChangedListener(DisplayResizeCallback callback) {
        addDisplayResizeCallback(callback);
    }



    /*
    @Override
    public void setContentScale(float x, float y) {
        throw NotImplementedException.NOT_IMPLEMENTED; // TODO
    }

    @Override
    public Vector2f getContentScale() {
        throw NotImplementedException.NOT_IMPLEMENTED; // TODO
    }

    @Override
    public void setVSync(boolean vSync) {
        if(!vSync) {
            glfwSwapInterval(0);
        }else {
            glfwSwapInterval(1);
        }
    }

     */

    @Override
    public void setVSync(boolean vSync) {
        if(!vSync) {
            glfwSwapInterval(0);
        }else {
            glfwSwapInterval(1);
        }
    }
}
