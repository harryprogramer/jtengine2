package thewall.engine.twilight;

import io.github.alexarchambault.windowsansi.WindowsAnsi;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fusesource.jansi.AnsiConsole;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL11;
import thewall.engine.twilight.audio.SoundMaster;
import thewall.engine.twilight.debugger.TEngineDebugger;
import thewall.engine.twilight.debugger.console.DebugConsole;
import thewall.engine.twilight.display.DisplayResizeCallback;
import thewall.engine.twilight.display.GLFWDisplay;
import thewall.engine.twilight.display.WindowResizeSystem;
import thewall.engine.twilight.material.Colour;
import thewall.engine.twilight.spatials.Light;
import thewall.engine.twilight.spatials.Spatial;
import thewall.engine.twilight.errors.InitializationException;
import thewall.engine.twilight.events.EventManager;
import thewall.engine.twilight.events.JTEEventManager;
import thewall.engine.twilight.gui.GuiRenderer;
import thewall.engine.twilight.gui.imgui.ImGUIGuard;
import thewall.engine.twilight.gui.imgui.ImGuiDesigner;
import thewall.engine.twilight.gui.imgui.ImmediateModeGUI;
import thewall.engine.twilight.hardware.Hardware;
import thewall.engine.twilight.hardware.PlatformEnum;
import thewall.engine.twilight.hardware.hna.RealtimeHNAccess;
import thewall.engine.twilight.input.Input;
import thewall.engine.twilight.input.InputProvider;
import thewall.engine.twilight.input.keyboard.KeyListener;
import thewall.engine.twilight.models.Loader;
import thewall.engine.twilight.renderer.MasterRenderer;
import thewall.engine.twilight.runtime.AbstractRuntime;
import thewall.engine.twilight.runtime.app.TEngineAppRuntime;
import thewall.engine.twilight.runtime.TwilightRuntimeService;
import thewall.engine.twilight.terrain.Terrain;
import thewall.engine.twilight.utils.WatchdogTimeMonitor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.lwjgl.glfw.GLFW.*;
import static thewall.engine.twilight.system.lwjgl.GLFWInputUtils.keyToEnum;

/**
 * Base class for engine app
 *
 * @deprecated due api rebuild from 11.17.2021, {@link thewall.engine.LegacyApp}
 */
@Deprecated(forRemoval = true)
@SuppressWarnings("unused")
public abstract class TwilightApp extends GLFWDisplay {
    private static final AtomicBoolean isInit = new AtomicBoolean(false);
    private final static Logger logger = LogManager.getLogger(TwilightApp.class);
    private final static PlatformEnum[] supportedPlatform = {PlatformEnum.WINDOWS, PlatformEnum.LINUX, PlatformEnum.MACOS};
    private volatile boolean isStackTraceEnabled = false;
    private volatile AbstractRuntime<TwilightApp> runtime;
    private static double previousTime = glfwGetTime();
    private final static int initTimeout = 15;
    private static long timeout = 15000;
    private PrintWriter logCallback;
    private Thread renderThread;

    private ImmediateModeGUI immediateModeGUI;

    @Setter
    @Getter
    private volatile WatchdogTimeMonitor watchdog;

    @Getter
    public static final String version = "1.1.0.3";

    @Getter
    @Setter
    private DebugConsole debugConsole = DebugConsole.getConsole();

    @Getter
    private volatile int frameLimit = 60; // DEFAULT FRAME LIMIT

    private static final Hardware hnaAccess = new RealtimeHNAccess();
    private static final Hardware asyncHNAccess = null; // TODO

    @Getter
    private String name = "App";

    @Getter
    private Light light = new Light(new Vector3f(0, 0, 0), Colour.WHITE, new Vector3f(0.4f, 0.4f, 0.4f));
    //@Getter
    //private Camera rndrCamera = new Camera();

    @Getter
    private final Loader loader = new Loader();

    @Getter
    @Setter
    private GuiRenderer guiRenderer;

    @Getter
    @Setter
    private volatile SoundMaster soundMaster;

    @Getter
    private int windowWidth = 1280, windowHeight = 720;

    @Getter
    @Setter
    private volatile MasterRenderer renderer;

    @Getter
    private final EventManager eventManager = new JTEEventManager();

    private WindowResizeSystem windowResizeSystem;

    private final AtomicBoolean isImGuiShow = new AtomicBoolean(false);

    @Setter
    private InputProvider input;

    public Input input(){
        checkInit();
        checkStart();
        return input;
    }

    /**
     * Set window size
     *
     * @param width width of display
     * @param height height of display
     *  */
    public void setSize(int width, int height){
        windowHeight = height;
        windowWidth = width;
    }

    public ImGuiDesigner getImmediateGUI(){
        return this.immediateModeGUI.getDesigner();
    }

    public void showImmediateGUI(){
        isImGuiShow.set(true);
    }

    public void hideImmediateGUI(){
        isImGuiShow.set(false);
    }


    public boolean isImmediateGUIHidden(){
        return isImGuiShow.get();
    }

    public void setImmediateModeGUI(ImmediateModeGUI gui){
        this.immediateModeGUI = new ImGUIGuard(this, gui);
    }

    /**
     * Set window title
     *
     * @param windowTitle window title
     * */
    public void setWindowTitle(String windowTitle){
        checkInit();
        glfwSetWindowTitle(getWindow(), windowTitle);
    }

    public Hardware getRealtimeHardware(){
        return hnaAccess;
    }

    public Hardware getIndexedAsyncHardware(){
        return asyncHNAccess;
    }

    public void enableVSync(){
        checkInit();
        super.enableVSync();
    }

    public void disableVSync(){
        checkInit();
        super.disableVSync();
    }

    public void setLogCallback(PrintWriter callback){
        logCallback = callback;
    }

    public void setKeyboardCallback(KeyListener KeyListener){
        checkInit();
        runtime.executeTask(() -> glfwSetKeyCallback(getWindow(), (window, key, scancode, action, mods) -> {
            try {
                KeyListener.invoke(keyToEnum(key), scancode, action, mods);
            }catch (Exception e){
                //logger.warn("Keyboard callback error " + e.getMessage());
            }
        })
        );
    }

    public void setWindowResizeSystem(WindowResizeSystem windowResizeSystem){
        Objects.requireNonNull(windowResizeSystem);
        this.windowResizeSystem = windowResizeSystem;
    }

    public void processTerrain(Terrain terrain){
        renderer.processTerrain(terrain);
    }

    public void processEntity(Spatial entity){
        renderer.processEntity(entity);
    }

    public void processLight(Light light){
        this.light = light;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void enableDebugStackTrace(){
        isStackTraceEnabled = true;
    }

    public void disableDebugStackTrace(){
        isStackTraceEnabled = false;
    }

    //public void rndrProcessCamera(Camera camera){
    //    this.rndrCamera = camera;
    //}

    @SneakyThrows
    public static void init(){
        class Dummy {
            public void m() {
            }
        }
        for (int i = 0; i < 10000000; i++) {
            Dummy dummy = new Dummy();
            dummy.m();
        }

        AnsiConsole.systemInstall();

        if (com.oracle.svm.core.os.IsDefined.WIN32()) {
            try {
                WindowsAnsi.setup();
            } catch (IOException e) {
                logger.warn("ANSI terminal colors unavailable to init", e);
            }

        }

        System.setOut(AnsiConsole.out());
        System.setErr(AnsiConsole.err());

        GLFWErrorCallback.createPrint(System.err).set();

        System.setProperty("log4j2.contextSelector", "org.apache.logging.log4j.core.async.AsyncLoggerContextSelector");

        logger.info("Initializing Twilight " + getVersion());

        glfwSetErrorCallback((error1, description) -> {
            logger.error("GLFW Error [" + error1 + "]: " + description);
        });

        boolean isSupported = false;
        for(PlatformEnum platform : supportedPlatform){
            if(platform ==  hnaAccess.getPlatform()){
                isSupported = true;
                break;
            }
        }

        if(!isSupported){
            logger.fatal("Unsupported platform [" + hnaAccess.getPlatform().getName() + "]", new InitializationException("Engine", "Unsp"));
        }

        TEngineDebugger.setPrintProxyDebugger(DebugConsole.getConsole());

        if(isInit.compareAndSet(false, true)) {
            TwilightRuntimeService.registerRuntime(TwilightApp.class, TEngineAppRuntime.class);
        }

        if(!glfwInit()){
            throw new InitializationException("Engine", "OpenGL GLFW init failed");
        }

        TwilightRuntimeService.init();
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_DOUBLEBUFFER, GLFW_TRUE); // FIXME czarny ekran na Windowsie (problem z podwojnym buforem obrazu), rozwiazanie tymczasowe: podwojne buforowanie zostalo włączone
    }
    public void setFPSLimit(int limit){
        frameLimit = limit;
    }

    public void stop(){
        runtime.forceStop();
    }

    private volatile boolean isError = false;
    private volatile Throwable error = null;

    private static @Nullable AbstractRuntime<TwilightApp> startRuntime(TwilightApp app){
        checkInit();
        AbstractRuntime<TwilightApp> runtime;
        runtime = TwilightRuntimeService.findRuntime(TwilightApp.class);
        if(runtime == null){
            throw new RuntimeException("No runtime found for TEngine app");
        }
        Thread thread = new Thread(() -> runtime.execute(app));
        thread.setName("TwLight Main Thread");

        thread.setUncaughtExceptionHandler((t, e) -> {
            logger.info(e);
            app.isError = true;
            app.error = e;
        });
        thread.start();

        long start = System.currentTimeMillis();

        while (!runtime.isReady()){
            if(app.isError){
                logger.fatal("Fatal error while starting the engine, there was an unexpected error during engine initialization.\n" +
                        "The logs above have more information", app.error);
                return null;
            }

            if((System.currentTimeMillis() - start) >= timeout){
                logger.fatal("Time for initialization has been exceeded, the engine did not start in the expected time.\nThe runtime abort run.");
                return null;
            }

            Thread.onSpinWait();
        }

        thread.setUncaughtExceptionHandler(null);


        return runtime;
    }

    public static boolean isInit(){
        return isInit.get();
    }

    public static void startApp(TwilightApp app){
        AbstractRuntime<TwilightApp> runtime;
        try {
            runtime = startRuntime(app);
        }catch (InitializationException e){
            logger.fatal("Engine not initialized", e);
            System.exit(1);
            return;
        }
        if(runtime == null){
            throw new InitializationException("Engine was not initialized probably, look logs upper");
        }
        app.runtime = runtime;

    }

    private static void checkInit(){
        if(!isInit.get()){
            throw new InitializationException("TwLight", TwilightApp.class);
        }

    }

    private void checkStart(){
        if(!isInit()){
            throw new InitializationException("Before changing some settings you must first run app.");
        }
    }

    public void enableAutoWindowResizable(){
        addDisplayResizeCallback(windowResizeSystem);
    }

    public void disableAutoWindowResizable(){
        unregisterResizeCallback(windowResizeSystem);
    }

    public void setWindowResizeCallback(DisplayResizeCallback callback){
        addDisplayResizeCallback(callback);
    }

    public void unregisterResizeCallback(DisplayResizeCallback callback){
        unregisterDisplayCallback(callback);
    }


    public static void setRuntimeTimeout(long time){
        if(time <= 0 || time > 50000){
            throw new IllegalStateException("Timeout is must not higher than 50000 and lower than 0");
        }

        timeout = time;
    }



    public static String getRenderAPIVersion(){
        return GL11.glGetString(GL11.GL_VERSION);
    }


    public abstract void onEnable();

    public abstract void onDisable();

    public abstract void update();
}
