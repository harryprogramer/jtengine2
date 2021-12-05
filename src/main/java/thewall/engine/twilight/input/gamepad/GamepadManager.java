package thewall.engine.twilight.input.gamepad;

import java.util.List;

public interface GamepadManager {
    List<GamepadNumber> availableControllers();

    Gamepad getController(GamepadNumber number);
}
