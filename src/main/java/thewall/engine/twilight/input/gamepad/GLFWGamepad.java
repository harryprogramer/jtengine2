package thewall.engine.twilight.input.gamepad;

import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class GLFWGamepad extends GamepadBuilder {
    private static final Logger logger = LogManager.getLogger(GLFWGamepad.class);
    private final int controller;

    private final static int[] controllerDictionary = {0, GLFW_JOYSTICK_1, GLFW_JOYSTICK_2, GLFW_JOYSTICK_3,
            GLFW_JOYSTICK_4, GLFW_JOYSTICK_5, GLFW_JOYSTICK_6, GLFW_JOYSTICK_7,
            GLFW_JOYSTICK_8, GLFW_JOYSTICK_9, GLFW_JOYSTICK_10, GLFW_JOYSTICK_11,
            GLFW_JOYSTICK_12, GLFW_JOYSTICK_13, GLFW_JOYSTICK_14, GLFW_JOYSTICK_15,
            GLFW_JOYSTICK_16};

    private String gamepadName = super.getGamepadName();

    public GLFWGamepad(int controller) {
        super(controller);
        this.controller = controller;

        getGamepadName();
    }

    @SneakyThrows
    @Override
    protected GamepadAxisPack getGamepadAxis() {
        FloatBuffer buffer = glfwGetJoystickAxes(controllerDictionary[controller]);
        if(buffer != null){
            return createAxisPack(createAxis(buffer.get(0), buffer.get(1)),
                    createAxis(buffer.get(2), buffer.get(3)), createAxis(buffer.get(4), buffer.get(5)));
        }

        logger.warn("GLFW buffer for GLFW Gamepad is null, returning (0,0) axis");
        return createAxisPack(createAxis(0, 0), createAxis(0, 0), createAxis(0, 0));
    }

    @Override
    public String getGamepadName() {
        String name = glfwGetJoystickName(controllerDictionary[controller]);
        if(name != null){
            this.gamepadName = name;
            return name;
        }

        return gamepadName;
    }

    @Override
    public boolean isGamepadStillPresent() {
        return glfwJoystickPresent(controllerDictionary[controller]);
    }

}
