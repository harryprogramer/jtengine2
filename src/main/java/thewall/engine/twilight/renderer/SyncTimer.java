package thewall.engine.twilight.renderer;

import org.lwjgl.glfw.GLFW;

public class SyncTimer {

    final static double
            NANO_RESOLUTION = 1000000000.0D,
            GLFW_RESOLUTION = 1.0D;

    public final static int
            JAVA_NANO = 1,
            LWJGL_GLFW = 2;

    private int mode;
    private double timeThen;
    private boolean enabled = true;

    public SyncTimer(int mode) {
        setNewMode(mode);
    }

    private double getResolution() {
        return switch (mode) {
            case JAVA_NANO -> NANO_RESOLUTION;
            case LWJGL_GLFW -> GLFW_RESOLUTION;
            default -> 0;
        };
    }

    private double getTime() {
        return switch (mode) {
            case JAVA_NANO -> System.nanoTime();
            case LWJGL_GLFW -> GLFW.glfwGetTime();
            default -> 0;
        };
    }


    public void setEnabled(boolean enable) { enabled = enable;}

    public boolean isEnabled() { return enabled; }

    public void setNewMode(int timerMode) {
        mode = timerMode;
        timeThen = getTime();
    }

    public String getModeString() {
        return switch (mode) {
            case JAVA_NANO -> "NANO";
            case LWJGL_GLFW -> "LWJGL";
            default -> null;
        };
    }

    public int getMode() {
        return mode;
    }

    public void sync(double fps) throws Exception {
        double resolution = getResolution();
        double timeNow =  getTime();
        int updates = 0;

        if (enabled) {
            double gapTo = resolution / fps + timeThen;

            while (gapTo < timeNow) {
                gapTo = resolution / fps + gapTo;
                updates++;
            }
            while (gapTo > timeNow) {
                Thread.sleep(1);
                timeNow = getTime();
            }
            updates++;

            timeThen = gapTo;
        } else {
            while (timeThen < timeNow) {
                timeThen = resolution / fps + timeThen;
                updates++;
            }
        }

    }
}
