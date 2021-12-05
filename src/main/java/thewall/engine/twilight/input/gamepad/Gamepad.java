package thewall.engine.twilight.input.gamepad;

public interface Gamepad {
    GamepadNumber getNumber();

    String getGamepadName();

    GamepadAxis getAxis1();

    GamepadAxis getAxis2();

    GamepadAxis getAxis3();

    boolean isGamepadStillPresent();
}
