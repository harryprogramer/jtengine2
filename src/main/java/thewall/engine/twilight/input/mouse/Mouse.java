package thewall.engine.twilight.input.mouse;

/**
 A raw TEngine mouse interface. This can be get mouse movement and scrolly wheel.
 @author many
 */
public interface Mouse {
    CursorPosition getCursorPosition();

    void setCursorPositionCallback(CursorPositionCallback callback) throws UnsupportedOperationException;

    void setCursorPosition(double posX, double posY);

    boolean getMouseKeyPress(MouseButtons button);

    boolean getMouseKeyReleased(MouseButtons button);

    void createMouseKeyCallback(TMouseCallback callback);

    void disableCursor();

    void hideCursor();

    void showCursor();

    boolean isCursorDisabled();

    boolean isCursorHidden();
}
