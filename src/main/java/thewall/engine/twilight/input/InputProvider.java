package thewall.engine.twilight.input;

import thewall.engine.twilight.input.gamepad.GamepadManager;
import thewall.engine.twilight.input.keyboard.Keyboard;
import thewall.engine.twilight.input.mouse.Mouse;

public class InputProvider implements Input{
    private GamepadManager gamepadManager;
    private Keyboard keyboard;
    private Mouse mouse;

    public InputProvider(){

    }

    public InputProvider(Keyboard keyboard, Mouse mouse, GamepadManager gamepadManager){
        this.gamepadManager = gamepadManager;
        this.keyboard = keyboard;
        this.mouse = mouse;
    }

    public void setGamepadManager(GamepadManager gamepadManager){
        this.gamepadManager = gamepadManager;
    }

    public void setKeyboard(Keyboard keyboard){
        this.keyboard = keyboard;
    }

    public void setMouse(Mouse mouse){
        this.mouse = mouse;
    }

    @Override
    public GamepadManager getGamepad(){
        return gamepadManager;
    }

    @Override
    public Keyboard getKeyboard() {
        return keyboard;
    }

    @Override
    public Mouse getMouse() {
        return mouse;
    }
}
