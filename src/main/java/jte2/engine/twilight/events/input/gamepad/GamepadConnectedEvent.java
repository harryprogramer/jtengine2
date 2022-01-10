package jte2.engine.twilight.events.input.gamepad;

import jte2.engine.twilight.events.AbstractEvent;
import jte2.engine.twilight.events.EventType;
import jte2.engine.twilight.input.gamepad.Gamepad;


public final class GamepadConnectedEvent extends AbstractEvent {
    private final Gamepad gamepad;

    public GamepadConnectedEvent(Gamepad gamepad) {
        super(true, EventType.INPUT);
        this.gamepad = gamepad;
    }

    public Gamepad getGamepad(){
        return gamepad;
    }

}
