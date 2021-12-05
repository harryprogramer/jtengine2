package thewall.engine.twilight.display;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;
import thewall.engine.twilight.TwilightApp;
import thewall.engine.twilight.errors.WindowFailureException;

import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;


public class GLFWDisplayManager implements GLDisplayManager {
    private static volatile long window;

    private volatile boolean isInit = false;

     int fps = 0;

    public long getFps() {
        return fps; // FIXME: return 0
    }

    private static long lastFrameTime;
    private static volatile float delta;
    private final int width, height;
    Random r = new Random();

    public static long pointer(){
        return window;
    }

    public boolean isInitialized(){
        return isInit;
    }

    public float random() {
        r = new Random();
        return .0f + r.nextFloat() * (.9f - .0f);
    }

    public GLFWDisplayManager(int width, int height){
        this.width = width;
        this.height = height;
    }

    public GLFWDisplayManager(){
        this.width = 1080;
        this.height = 720;
    }

    @Override
    public String toString() {
        return "DisplayManager{" +
                "window=" + window +
                ", fps=" + fps +
                ", width=" + width +
                ", height=" + height +
                ", r=" + r +
                '}';
    }

    @Override
    public void createDisplay(int width, int height, String name){
        window = GLFW.glfwCreateWindow(width, height, name, MemoryUtil.NULL, MemoryUtil.NULL);
        if(window == MemoryUtil.NULL){
            throw new WindowFailureException("window is null");
        }
        glfwMakeContextCurrent(window);

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE ) {
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
            }

            if(key == GLFW_KEY_F3 && action == GLFW_RELEASE){
                if(glfwGetInputMode(window, GLFW_CURSOR) == GLFW_CURSOR_NORMAL){
                    glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
                }else {
                    glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
                }
            }
        });

        GL.createCapabilities();

        lastFrameTime = System.currentTimeMillis();
    }


    @Override
    public void updateDisplay() {
        glfwSwapBuffers(window);

        glfwPollEvents();

        long currentFrameTime = System.currentTimeMillis();
        delta = currentFrameTime - lastFrameTime;
        lastFrameTime = currentFrameTime;
    }

    @Deprecated
    public static float getFrameTimeSeconds(){
        return delta;
    }

    @Override
    public void enableVSync(){
        glfwSwapInterval(1);
    }

    @Override
    public void disableVSync(){
        glfwSwapInterval(0);
    }

    @Override
    public long getWindow() {
        return window;
    }
}
