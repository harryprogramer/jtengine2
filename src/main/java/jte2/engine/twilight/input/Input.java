package jte2.engine.twilight.input;

import jte2.engine.twilight.input.gamepad.GamepadManager;
import jte2.engine.twilight.input.keyboard.Keyboard;
import jte2.engine.twilight.input.mouse.Mouse;

public interface Input {
    GamepadManager getGamepad();

    Keyboard getKeyboard();

    Mouse getMouse();
}
