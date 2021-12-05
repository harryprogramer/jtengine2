package thewall.engine.twilight.events.input.gamepad;

import thewall.engine.twilight.events.AbstractEvent;
import thewall.engine.twilight.events.EventType;
import thewall.engine.twilight.input.gamepad.Gamepad;

public class GamepadDisconnectedEvent extends AbstractEvent {
    private final Gamepad gamepad;

    public GamepadDisconnectedEvent(Gamepad gamepad){
        super(true, EventType.INPUT);
        this.gamepad = gamepad;
    }

    public Gamepad getGamepad(){
        return gamepad;
    }
}
