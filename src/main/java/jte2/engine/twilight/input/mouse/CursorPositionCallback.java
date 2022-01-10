package jte2.engine.twilight.input.mouse;

@FunctionalInterface
public interface CursorPositionCallback {
    void invoke(double xpos, double ypos);
}
