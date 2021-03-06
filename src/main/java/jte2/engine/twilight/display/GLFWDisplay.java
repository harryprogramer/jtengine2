package jte2.engine.twilight.display;

import jte2.engine.twilight.texture.Picture;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;
import org.lwjgl.BufferUtils;
import jte2.engine.twilight.Area;
import jte2.engine.twilight.errors.NotImplementedException;
import jte2.engine.twilight.utils.SafeArrayList;
import org.lwjgl.glfw.GLFWImage;

import java.awt.image.BufferedImage;
import java.nio.IntBuffer;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static org.lwjgl.glfw.GLFW.*;

public class GLFWDisplay extends GLFWDisplayManager implements Display {
    private final static Logger logger = LogManager.getLogger(GLFWDisplay.class);
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
    public void setSize(Area area) {
        logger.info("Setting display size to " + area);
        glfwSetWindowSize(getWindow(), area.getWidth(), area.getHeight());
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
    public void setMinimumSize(@NotNull Area area) {
        if(area.getWidth() <= 0 || area.getHeight() <= 0){
            throw new IllegalStateException("width or height is must greater than 0");
        }

        glfwSetWindowSizeLimits(getWindow(), area.getWidth(), area.getHeight(), GLFW_DONT_CARE, GLFW_DONT_CARE);
    }

    @Override
    public void setMaximumSize(@NotNull Area area) {
        if(area.getWidth() <= 0 || area.getHeight() <= 0){
            throw new IllegalStateException("width or height is must greater than 0");
        }
        glfwSetWindowSizeLimits(getWindow(), GLFW_DONT_CARE, GLFW_DONT_CARE, area.getWidth(), area.getHeight());
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
    public void setIcon(Picture picture) {
        GLFWImage.Buffer imagebf = GLFWImage.malloc(1);
        GLFWImage image = GLFWImage.malloc();
        image.set(picture.getWidth(), picture.getHeight(), picture.getImageBuffer());
        imagebf.put(0, image);
        glfwSetWindowIcon(getWindow(), imagebf);
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
