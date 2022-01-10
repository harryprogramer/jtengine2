package jte2.engine.twilight.input.glfw;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import jte2.engine.twilight.input.InputProvider;
import jte2.engine.twilight.input.gamepad.GLFWGamepadManager;
import jte2.engine.twilight.input.gamepad.GLFWJoystickCallback;
import jte2.engine.twilight.input.gamepad.GamepadLookupService;
import jte2.engine.twilight.input.gamepad.GamepadManager;
import jte2.engine.twilight.input.keyboard.Keyboard;
import jte2.engine.twilight.input.keyboard.GLFWKeyboard;
import jte2.engine.twilight.input.mouse.Mouse;
import jte2.engine.twilight.input.mouse.GLFWMouse;
import jte2.engine.twilight.system.context.opengl.lwjgl.LegacyLwjglContext;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Class for create and node all GLFW input types
 */
public class GLFWInput extends InputProvider {
    private static final Logger logger = LogManager.getLogger(GLFWInput.class);

    private GamepadManager gamepadManager;
    private Keyboard keyboard;
    private Mouse mouse;

    public GLFWInput(LegacyLwjglContext lwjglContext) {
        GamepadLookupService gamepadLookupService = new GamepadLookupService();
        glfwSetJoystickCallback(new GLFWJoystickCallback(lwjglContext ,gamepadLookupService));
        setGamepadManager(new GLFWGamepadManager(gamepadLookupService));
        setKeyboard(new GLFWKeyboard(lwjglContext));
        setMouse(new GLFWMouse(lwjglContext));
    }
}
