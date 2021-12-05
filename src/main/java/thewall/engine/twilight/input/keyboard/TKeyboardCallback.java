package thewall.engine.twilight.input.keyboard;

@FunctionalInterface
public interface TKeyboardCallback {
    void invoke(KeyboardKeys key, int scancode, int action, int mods);
}
