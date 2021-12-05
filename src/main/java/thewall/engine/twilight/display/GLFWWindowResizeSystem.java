package thewall.engine.twilight.display;

import thewall.engine.twilight.renderer.MasterRenderer;

public class GLFWWindowResizeSystem extends WindowResizeSystem {
    private final MasterRenderer masterRenderer;

    public GLFWWindowResizeSystem(MasterRenderer masterRenderer){
        this.masterRenderer = masterRenderer;
    }

    @Override
    void resizeWindow(int x, int y) {
        masterRenderer.resizeWindow(x, y);
    }
}
