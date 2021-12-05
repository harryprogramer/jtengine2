package thewall.engine.twilight.input.gamepad;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFWJoystickCallbackI;
import thewall.engine.twilight.events.input.gamepad.GamepadConnectedEvent;
import thewall.engine.twilight.events.input.gamepad.GamepadDisconnectedEvent;
import thewall.engine.twilight.system.lwjgl.LegacyLwjglContext;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class GLFWJoystickCallback implements GLFWJoystickCallbackI {

    private final GamepadLookupService gamepadLookupService;

    private final LegacyLwjglContext context;


    public GLFWJoystickCallback(LegacyLwjglContext context, GamepadLookupService gamepadLookupService){
        this.context = context;
        this.gamepadLookupService = gamepadLookupService;

        List<Integer> controllers = getActiveControllers();
        for(int controller : controllers){
            gamepadLookupService.createGamepad(controller);
            this.context.getEventManager().callEvent(new GamepadConnectedEvent(new GLFWGamepad(controller)));
        }
    }

    @Override
    public void invoke(int jid, int event) {
        jid++;
        if(event == GLFW_CONNECTED){
            context.getEventManager().callEvent(new GamepadConnectedEvent(gamepadLookupService.createGamepad(jid)));
        }else if(event == GLFW_DISCONNECTED){
            Gamepad gamepad = gamepadLookupService.getGamepad(jid);
            context.getEventManager().callEvent(new GamepadDisconnectedEvent(gamepadLookupService.getGamepad(jid)));
            gamepadLookupService.deleteGamepad(gamepad.getNumber().getRawNumber());
        }
    }

    private static @NotNull List<Integer> getActiveControllers(){
        int[] controllerDictionary = {GLFW_JOYSTICK_1, GLFW_JOYSTICK_2, GLFW_JOYSTICK_3,
                GLFW_JOYSTICK_4, GLFW_JOYSTICK_5, GLFW_JOYSTICK_6, GLFW_JOYSTICK_7,
                GLFW_JOYSTICK_8, GLFW_JOYSTICK_9, GLFW_JOYSTICK_10, GLFW_JOYSTICK_11,
                GLFW_JOYSTICK_12, GLFW_JOYSTICK_13, GLFW_JOYSTICK_14, GLFW_JOYSTICK_15,
                GLFW_JOYSTICK_16};

        List<Integer> controllers = new ArrayList<>();

        for(int i = 0; i < 15; i++){
            if(glfwJoystickPresent(controllerDictionary[i])){
                controllers.add(i + 1);
            }
        }

        return controllers;
    }


}
