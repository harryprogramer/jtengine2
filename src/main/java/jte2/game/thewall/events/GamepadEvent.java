package jte2.game.thewall.events;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import jte2.engine.twilight.events.Event;
import jte2.engine.twilight.events.EventPriority;
import jte2.engine.twilight.events.EventType;
import jte2.engine.twilight.events.Listener;
import jte2.engine.twilight.events.input.gamepad.GamepadConnectedEvent;
import jte2.engine.twilight.events.input.gamepad.GamepadDisconnectedEvent;

@SuppressWarnings("unused")
public class GamepadEvent implements Listener {

    @SneakyThrows
    @Event(priority = EventPriority.NORMAL, type = EventType.INPUT)
    public void onNewController(@NotNull GamepadConnectedEvent gamepadConnectedEvent){
        System.out.println("gamepad: " + gamepadConnectedEvent.getGamepad().getGamepadName());
    }

    @Event(priority = EventPriority.NORMAL, type = EventType.INPUT)
    public void onDisconnect(@NotNull GamepadDisconnectedEvent gamepadDisconnectedEvent){
        System.out.println("gamepad disconnected: " + gamepadDisconnectedEvent.getGamepad().getGamepadName());
    }
}
