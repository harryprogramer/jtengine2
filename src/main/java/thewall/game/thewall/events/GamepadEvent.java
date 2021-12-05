package thewall.game.thewall.events;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import thewall.engine.twilight.events.Event;
import thewall.engine.twilight.events.EventPriority;
import thewall.engine.twilight.events.EventType;
import thewall.engine.twilight.events.Listener;
import thewall.engine.twilight.events.input.gamepad.GamepadConnectedEvent;
import thewall.engine.twilight.events.input.gamepad.GamepadDisconnectedEvent;

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
