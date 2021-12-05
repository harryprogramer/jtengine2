package thewall.engine.twilight.display;

public interface DisplayManager {
    void createDisplay(int width, int height, String name);

    void updateDisplay() throws Exception;

    void enableVSync();

    void disableVSync();
}
