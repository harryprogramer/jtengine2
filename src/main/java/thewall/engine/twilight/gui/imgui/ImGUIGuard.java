package thewall.engine.twilight.gui.imgui;

import org.jetbrains.annotations.NotNull;
import thewall.engine.twilight.TwilightApp;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public final class ImGUIGuard implements ImGuiDesigner, ImmediateModeGUI {
    private final List<String> windowNames = new ArrayList<>();
    private boolean isWindowStarted = false;

    private final List<Method> rootMethod;
    private final ImmediateModeGUI gui;

    public ImGUIGuard(TwilightApp app, ImmediateModeGUI gui){
        this.gui = gui;
        this.rootMethod = getMethodsAnnotatedWith(app);
    }

    private static @NotNull List<Method> getMethodsAnnotatedWith(final @NotNull Object type) {
        final List<Method> methods = new ArrayList<>();
        Class<?> klass = type.getClass();
        while (klass != Object.class) {
            for (final Method method : klass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(OnImmediateGUI.class)) {
                    methods.add(method);
                }
            }
            klass = klass.getSuperclass();
        }
        return methods;
    }


    private void checkCallers(){
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        for(StackTraceElement stackTraceElement : stackTraceElements) {
            for (Method method : rootMethod) {
                if (stackTraceElement.getMethodName().equals(method.getName())){
                    return;
                }
            }
        }

        throw new IllegalStateException("Immediate GUI can be called only from OnImmediateGUI annotation function");
    }

    private void checkWindowInit(){
        if(!isWindowStarted)
            throw new IllegalStateException("window not set");
    }

    @Override
    public void beginWindow(String window) {
        checkCallers();
        if(!isWindowStarted) {
            if(windowNames.contains(window)){
                throw new IllegalStateException("Window [" + window + "] is already defined in this frame");
            }
            isWindowStarted = true;
            windowNames.add(window);
            gui.getDesigner().beginWindow(window);
        }else {
            throw new IllegalStateException("Window begin is already started");
        }
    }

    @Override
    public void endWindow() {
        checkCallers();
        if(isWindowStarted) {
            isWindowStarted = false;
            gui.getDesigner().endWindow();
        }else {
            throw new IllegalStateException("Window is already stopped");
        }
    }

    @Override
    public void textSameLine(String text) {
        checkCallers();
        checkWindowInit();
        gui.getDesigner().textSameLine(text);
    }

    @Override
    public void text(String text) {
        checkCallers();
        checkWindowInit();
        gui.getDesigner().text(text);
    }

    @Override
    public void plotLines(String text, float[] values, float scaleMin, float scaleMax, float heightX, float heightY) {
        checkCallers();
        checkWindowInit();
        gui.getDesigner().plotLines(text, values, scaleMin, scaleMax, heightX, heightY);
    }

    @Override
    public void plotHistogram(String text, float[] values, float scaleMin, float scaleMax, float heightX, float heightY) {
        checkCallers();
        checkWindowInit();
        gui.getDesigner().plotHistogram(text, values, scaleMin, scaleMax, heightX, heightY);
    }

    @Override
    public boolean button(String text) {
        checkCallers();
        checkWindowInit();
        return gui.getDesigner().button(text);
    }

    @Override
    public void init() {
        gui.init();
    }

    @Override
    public void destroy() {
        gui.destroy();
    }

    @Override
    public void renderBegin() {
        gui.renderBegin();
    }

    @Override
    public void renderEnd() {
        windowNames.clear();
        gui.renderEnd();
    }

    @Override
    public ImGuiDesigner getDesigner() {
        return gui.getDesigner();
    }

    @Override
    public void beginWindow(String window, float x, float y) {
        checkCallers();
        gui.getDesigner().beginWindow(window, x, y);
    }
}
