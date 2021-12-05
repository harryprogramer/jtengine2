package thewall.engine.twilight.gui.imgui;

public interface ImGuiDesigner {
    // TODO add the remaining functions for imgui

    void beginWindow(String window);

    void beginWindow(String window, float x, float y);

    void endWindow();

    void textSameLine(String text);

    void text(String text);

    void plotLines(String text, float[] values, float scaleMin, float scaleMax, float heightX, float heightY);

    void plotHistogram(String text, float[] values, float scaleMin, float scaleMax, float heightX, float heightY);

    boolean button(String text);
}
