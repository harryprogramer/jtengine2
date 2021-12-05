package thewall.engine.twilight.input.mouse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static org.lwjgl.glfw.GLFW.*;

public enum MouseButtons {
    MOUSE_BUTTON_1,
    MOUSE_BUTTON_2,
    MOUSE_BUTTON_3,
    MOUSE_BUTTON_4,
    MOUSE_BUTTON_5,
    MOUSE_BUTTON_6,
    MOUSE_BUTTON_7,
    MOUSE_BUTTON_8
    ;

    private final static Logger logger = LogManager.getLogger(MouseButtons.class);

    @Contract(pure = true)
    public static @Nullable MouseButtons buttonToEnum(int button){
        switch (button){
            case GLFW_MOUSE_BUTTON_1 -> {
                return MOUSE_BUTTON_1;
            }

            case GLFW_MOUSE_BUTTON_2 -> {
                return MOUSE_BUTTON_2;
            }

            case GLFW_MOUSE_BUTTON_3 -> {
                return MOUSE_BUTTON_3;
            }

            case GLFW_MOUSE_BUTTON_4 -> {
                return MOUSE_BUTTON_4;
            }

            case GLFW_MOUSE_BUTTON_5 -> {
                return MOUSE_BUTTON_5;
            }

            case GLFW_MOUSE_BUTTON_6 -> {
                return MOUSE_BUTTON_6;
            }

            case GLFW_MOUSE_BUTTON_7 -> {
                return MOUSE_BUTTON_7;
            }

            case GLFW_MOUSE_BUTTON_8 -> {
                return MOUSE_BUTTON_8;
            }

            default -> {
                logger.warn("Invaild mouse key id: " + button);
                return null;
            }
        }
    }

    @Contract(pure = true)
    public static int buttonToEnum(@NotNull MouseButtons button){
        switch (button){
            case MOUSE_BUTTON_1 -> {
                return GLFW_MOUSE_BUTTON_1;
            }

            case MOUSE_BUTTON_2 -> {
                return GLFW_MOUSE_BUTTON_2;
            }

            case MOUSE_BUTTON_3 -> {
                return GLFW_MOUSE_BUTTON_3;
            }

            case MOUSE_BUTTON_4 -> {
                return GLFW_MOUSE_BUTTON_4;
            }

            case MOUSE_BUTTON_5 -> {
                return GLFW_MOUSE_BUTTON_5;
            }

            case MOUSE_BUTTON_6 -> {
                return GLFW_MOUSE_BUTTON_6;
            }

            case MOUSE_BUTTON_7 -> {
                return GLFW_MOUSE_BUTTON_7;
            }

            case MOUSE_BUTTON_8 -> {
                return GLFW_MOUSE_BUTTON_8;
            }

            default -> {
                logger.warn("Unknown mouse enum key: " + button.name());
                return 0;
            }
        }
    }
}
