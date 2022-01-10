package jte2.engine.twilight.display;

import jte2.engine.twilight.renderer.MasterRenderer;

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
