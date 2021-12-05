package thewall.engine.twilight.input.gamepad;

import java.util.List;

public class GLFWGamepadManager implements GamepadManager {
    private final GamepadLookupService gamepadLookupService;

    public GLFWGamepadManager(GamepadLookupService gamepadLookupService){
        this.gamepadLookupService = gamepadLookupService;
    }

    @Override
    public List<GamepadNumber> availableControllers() {
        return gamepadLookupService.availableControllers();
    }

    @Override
    public Gamepad getController(GamepadNumber number) {
        return gamepadLookupService.getGamepad(number.getRawNumber());
    }
}
