package jte2.engine.twilight.system.context.opengl.lwjgl;

import jte2.engine.twilight.system.JTESystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallbackI;

import static org.lwjgl.glfw.GLFW.*;

public class GLFWErrorCallback implements GLFWErrorCallbackI {
    private final Logger logger;

    public GLFWErrorCallback(Logger logger){
        this.logger = logger;
    }

    @Contract(pure = true)
    public static @NotNull String translateGLFWError(int error){
        switch (error){
            case GLFW_NO_ERROR -> {return "GLFW_NO_ERROR";}
            case GLFW_NOT_INITIALIZED -> {return "GLFW_NOT_INITIALIZED";}
            case GLFW_NO_CURRENT_CONTEXT -> {return "GLFW_NO_CURRENT_CONTEXT";}
            case GLFW_INVALID_ENUM -> {return "GLFW_INVALID_ENUM";}
            case GLFW_INVALID_VALUE -> {return "GLFW_INVALID_VALUE";}
            case GLFW_OUT_OF_MEMORY -> {return "GLFW_OUT_OF_MEMORY";}
            case GLFW_API_UNAVAILABLE -> {return "GLFW_API_UNAVAILABLE";}
            case GLFW_VERSION_UNAVAILABLE -> {return "GLFW_VERSION_UNAVAILABLE";}
            case GLFW_PLATFORM_ERROR -> {return "GLFW_PLATFORM_ERROR";}
            case GLFW_FORMAT_UNAVAILABLE -> {return "GLFW_FORMAT_UNAVAILABLE";}
            case GLFW_NO_WINDOW_CONTEXT -> {return "GLFW_NO_WINDOW_CONTEXT";}
            default -> {return "UNKNOWN";}
        }
    }

    @Override
    public void invoke(int error, long description) {
        String desc = org.lwjgl.glfw.GLFWErrorCallback.getDescription(description);
        logger.error("[GLFW] [" + translateGLFWError(error) + "]: " + desc);
        if(error == GLFW_API_UNAVAILABLE){
            JTESystem.showErrorDialog("JTEngine Problem", "Your graphics card does not support OpenGL features.\n" +
                    "Try to update the driver for your graphics card or restart the computer\n" +
                    "Details: " + desc);
        }
    }
}
