package thewall.engine.twilight.input;

import thewall.engine.twilight.input.gamepad.GamepadManager;
import thewall.engine.twilight.input.keyboard.Keyboard;
import thewall.engine.twilight.input.mouse.Mouse;

public interface Input {
    GamepadManager getGamepad();

    Keyboard getKeyboard();

    Mouse getMouse();
}
