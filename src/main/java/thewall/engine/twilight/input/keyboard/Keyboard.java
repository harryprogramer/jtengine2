package thewall.engine.twilight.input.keyboard;

public interface Keyboard {
    boolean isKeyPressed(KeyboardKeys key);

    boolean isKeyReleased(KeyboardKeys key);
}
