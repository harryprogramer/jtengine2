package jte2.engine.twilight.gui.imgui;

public interface ImmediateModeGUI {
    String GUI_ENDPOINT = "imgui_endpoint";

    void init();

    void destroy();

    void renderBegin();

    void renderEnd();

    ImGuiDesigner getDesigner();
}
