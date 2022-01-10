package jte2.engine.twilight.input.keyboard;

@FunctionalInterface
public interface KeyListener {
    void invoke(KeyboardKeys key, int scancode, int action, int mods);
}
