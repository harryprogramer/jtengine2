package thewall.engine.twilight.input.gamepad;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class GamepadLookupService {
    private final ConcurrentMap<GamepadNumber, Gamepad> gamepadMap = new ConcurrentHashMap<>();

    public Gamepad createGamepad(int controller){
        Gamepad gamepad = new GLFWGamepad(controller);
        gamepadMap.put(GamepadNumber.numberToGamepadNumber(controller), gamepad);
        return gamepad;
    }

    public Gamepad getGamepad(int controller){
        return gamepadMap.get(GamepadNumber.numberToGamepadNumber(controller));
    }

    public void deleteGamepad(int controller){
        gamepadMap.remove(GamepadNumber.numberToGamepadNumber(controller));
    }

    public List<GamepadNumber> availableControllers(){
        return gamepadMap.keySet().stream().toList();
    }
}
