package thewall.engine.twilight.gui.imgui;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;
import thewall.engine.twilight.display.GLDisplayManager;

public final class DearImmediateGUIMode implements ImmediateModeGUI, ImGuiDesigner {

    private final static Logger logger = LogManager.getLogger(DearImmediateGUIMode.class);

    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    private Thread renderThread = null;

    private boolean isInit = false;


    private final GLDisplayManager displayManager;
    private final String glslVersion;

    boolean showText = false;

    public DearImmediateGUIMode(GLDisplayManager displayManager, String glslVersion){
        this.displayManager = displayManager;
        this.glslVersion = glslVersion;
    }


    @Override
    public void init() {
        ImGui.createContext();
        ImGuiIO io = ImGui.getIO();
        io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);
        imGuiGlfw.init(displayManager.getWindow(), true);
        imGuiGl3.init(glslVersion);
        isInit = true;
    }


    @Override
    public void destroy() {
        imGuiGl3.dispose();
        imGuiGlfw.dispose();
        ImGui.destroyContext();
    }


    @Override
    public void renderEnd() {
        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());

        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final long backupWindowPtr = org.lwjgl.glfw.GLFW.glfwGetCurrentContext();
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
            GLFW.glfwMakeContextCurrent(backupWindowPtr);
        }
    }

    @Override
    public void renderBegin() {
        if (renderThread == null) {
            renderThread = Thread.currentThread();
        }

        imGuiGlfw.newFrame();
        ImGui.newFrame();
    }


    @Override
    public ImGuiDesigner getDesigner() {
        return this;
    }

    private void checkThread(){
        if(renderThread != null && Thread.currentThread() != renderThread) {
            throw new IllegalThreadStateException("this function can be use only from render thread");
        }
    }

    @Override
    public void beginWindow(String window) {
        checkThread();
        ImGui.begin(window);
    }

    @Override
    public void endWindow() {
        checkThread();
        ImGui.end();
    }

    @Override
    public void textSameLine(String text) {
        checkThread();
        ImGui.text(text);
        ImGui.sameLine();
    }

    @Override
    public boolean button(String text) {
        checkThread();
        return ImGui.button(text);
    }

    @Override
    public void beginWindow(String window, float x, float y) {
        ImGui.setNextWindowSize(x, y);
        beginWindow(window);
    }

    @Override
    public void text(String text) {
        ImGui.text(text);
    }

    @Override
    public void plotLines(String text, float[] values, float scaleMin, float scaleMax, float heightX, float heightY) {
        ImGui.plotLines(text, values, values.length, 0, "",scaleMin, scaleMax, heightX, heightY);
    }

    @Override
    public void plotHistogram(String text, float[] values, float scaleMin, float scaleMax, float heightX, float heightY) {
        ImGui.plotHistogram(text, values, values.length, 0, "",scaleMin, scaleMax, heightX, heightY);
    }

}
