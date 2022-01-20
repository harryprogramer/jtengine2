package jte2.engine;

import io.github.alexarchambault.windowsansi.WindowsAnsi;
import jte2.engine.twilight.utils.Validation;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fusesource.jansi.AnsiConsole;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFWErrorCallback;
import jte2.engine.twilight.Application;
import jte2.engine.twilight.shaders.ShaderHandle;
import jte2.engine.twilight.shaders.gl.UnshadedShader;
import jte2.engine.twilight.viewport.Node;
import jte2.engine.twilight.viewport.Node2D;
import jte2.engine.twilight.viewport.ViewPort;
import jte2.engine.twilight.assets.AssetManager;
import jte2.engine.twilight.audio.SoundMaster;
import jte2.engine.twilight.debugger.TEngineDebugger;
import jte2.engine.twilight.debugger.console.DebugConsole;
import jte2.engine.twilight.display.Display;
import jte2.engine.twilight.spatials.Camera;
import jte2.engine.twilight.errors.InitializationException;
import jte2.engine.twilight.events.EventManager;
import jte2.engine.twilight.hardware.Hardware;
import jte2.engine.twilight.hardware.PlatformEnum;
import jte2.engine.twilight.input.Input;
import jte2.engine.twilight.runtime.AbstractRuntime;
import jte2.engine.twilight.runtime.TwilightRuntimeService;
import jte2.engine.twilight.runtime.app.JTEEnvironment;
import jte2.engine.twilight.system.AppSettings;
import jte2.engine.twilight.system.JTESystem;
import jte2.engine.twilight.viewport.ViewPort2D;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.lwjgl.glfw.GLFW.*;
import static jte2.engine.twilight.utils.Validation.checkNull;


public abstract class LegacyApp implements Application {
    private final static PlatformEnum[] supportedPlatform = {PlatformEnum.WINDOWS, PlatformEnum.LINUX, PlatformEnum.MACOS};
    private static final AtomicBoolean isInit = new AtomicBoolean(false);
    private final static Logger logger = LogManager.getLogger(LegacyApp.class);
    private final Hardware hardware = JTESystem.createBestHardware();
    private ShaderHandle shader = new UnshadedShader();
    public final Node2D guiNode = new Node2D();
    public final Node rootNode = new Node();
    private AssetManager assetManager;
    private EventManager eventManager;
    public final ViewPort2D guiViewPort;
    public final ViewPort viewPort;
    private int fps = 0;

    static {
        initialize();
    }

    @Setter private int frameLimit = 60;
    @Getter private Display display;

    private AppSettings appSettings;

    private SoundMaster sound;
    private Input input;

    public LegacyApp(){
        this.viewPort = new ViewPort();
        this.guiViewPort = new ViewPort2D();
        this.appSettings = new AppSettings();
    }

    public void setSettings(AppSettings appSettings) {
        Validation.checkNull(appSettings);
        this.appSettings = appSettings;
    }

    @Override
    public void setAssetsManager(AssetManager manager) {
        this.assetManager = manager;
    }

    public AssetManager getAssetsManager() {
        return assetManager;
    }

    @Override
    public void setEventManager(EventManager eventManager){
        if(this.eventManager != null){
            throw new IllegalStateException("Event manager is already set.");
        }
        checkNull(eventManager);
        this.eventManager = eventManager;
    }

    @Override
    public int getFrameLimit() {
        return frameLimit;
    }

    public Input getInput() {
        return input;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    @Override
    public AppSettings getSettings() {
        return appSettings;
    }

    @Override
    public void onInit() {
        viewPort.attachScene(rootNode);
        viewPort.setCamera(new Camera());
        guiViewPort.attachScene(guiNode);
        guiViewPort.setCamera(new Camera());
        init();
    }

    public synchronized void setShader(ShaderHandle shader) {
        if(shader == null){
            throw new NullPointerException("Shader is null");
        }
        this.shader = shader;
    }

    @Override
    public ShaderHandle getShader() {
        return shader;
    }

    @Override
    public Hardware getHardware() {
        return hardware;
    }

    @Override
    public void setInput(Input input) {
        this.input = input;
    }

    @Override
    public void setDisplay(Display display) {
        this.display = display;
    }

    @Override
    public void setSound(SoundMaster sound) {
        checkNull(sound);
        this.sound = sound;
    }

    @Override
    public void onClose() {
        onExit();
    }

    public void onExit(){}

    private long lastUpdate = System.currentTimeMillis();
    private int frames;
    @Override
    public void update() {
        frames++;
        long endTime = System.currentTimeMillis();
        if(endTime - lastUpdate > 1000){
            fps = frames;
            frames = 0;
            lastUpdate = System.currentTimeMillis();
        }
        onUpdate();
    }

    @Override
    public ViewPort2D getGUIViewPort() {
        return guiViewPort;
    }

    @Override
    public ViewPort getViewPort() {
        return viewPort;
    }

    public SoundMaster getSound() {
        return sound;
    }

    private volatile Throwable error;
    private volatile boolean isError = false;

    private @Nullable AbstractRuntime<LegacyApp> findRuntime(){
        int timeout = 25000;

        AbstractRuntime<LegacyApp> runtime;
        runtime = TwilightRuntimeService.findRuntime(LegacyApp.class);
        if(runtime == null){
            throw new RuntimeException("No runtime found, did the app is initialized?");
        }


        logger.info("Found runtime [" + runtime.getClass().getName() + "], executing program...");
        Thread thread = new Thread(() -> runtime.execute(this));
        thread.setName("Twilight");

        logger.debug("Runtime thread exception guard is ready");
        thread.setUncaughtExceptionHandler((t, e) -> {
            logger.error(String.format("[%s] Thread error", t), e);
            isError = true;
            error = e;
        });

        logger.debug("Starting runtime thread");
        thread.start();

        long start = System.currentTimeMillis();

        logger.debug("Waiting for runtime to continue");
        while (!runtime.isReady()){
            if(isError){
                logger.fatal("Unexpected Twilight runtime fault, aborting launch. [" +  error + "]");
                if(!thread.isAlive()){
                    logger.debug("Thread is alive, interrupting...");
                    thread.interrupt();
                }
                return null;
            }

            if((System.currentTimeMillis() - start) >= timeout){
                logger.fatal("Runtime initialize timeout, aborting...");
                return null;
            }

            Thread.onSpinWait();
        }

        logger.info("Runtime ready to work, initialization complet");

        thread.setUncaughtExceptionHandler(null);


        return runtime;
    }


    private void startApp(){
        AbstractRuntime<LegacyApp> runtime;
        logger.info("Starting application, searching for runtime...");
        logger.debug("Finding runtime to run app");
        try {
            runtime = findRuntime();
            if(runtime == null){
                logger.fatal("Engine was not initialized probably, runtime is null");
                System.exit(-1);
            }
        }catch (Exception e){
            logger.fatal("Startup failure", e);
            System.exit(1);
        }

        //app.runtime = runtime;
    }

    public String getName(){
        return JTESystem.NAME;
    }

    public String getVersion(){
        return JTESystem.VERSION;
    }

    private static void initialize(){
        if(!isInit.get()){
            logger.info("Initializing Twilight " + JTESystem.NAME);
            logger.debug("Warming up JVM...");

            class Dummy {
                public void m() {
                }
            }
            for (int i = 0; i < 10000000; i++) {
                Dummy dummy = new Dummy();
                dummy.m();
            }

            logger.debug("Installing ANSI Console");
            AnsiConsole.systemInstall();

            if (com.oracle.svm.core.os.IsDefined.WIN32()) {
                logger.debug("WIN32 WindowsANSI setting up");
                try {
                    WindowsAnsi.setup();
                } catch (IOException e) {
                    logger.warn("ANSI terminal colors unavailable to init", e);
                }

            }
            System.setOut(AnsiConsole.out());
            System.setErr(AnsiConsole.err());

            //System.setProperty("log4j2.contextSelector", "org.apache.logging.log4j.core.async.AsyncLoggerContextSelector");

            TEngineDebugger.setPrintProxyDebugger(DebugConsole.getConsole());

            logger.debug("Starting runtime service");
            TwilightRuntimeService.init();

            if(isInit.compareAndSet(false, true)) {
                TwilightRuntimeService.registerRuntime(LegacyApp.class, JTEEnvironment.class);
                logger.debug("Registering runtime service");
            }

            if(!glfwInit()){
                throw new InitializationException("Engine", "OpenGL GLFW init failed");
            }

            isInit.set(true);
        }

    }

    public void start(){
        boolean isSupported = false;
        for(PlatformEnum platform : supportedPlatform){
            if(platform ==  hardware.getPlatform()){
                isSupported = true;
                break;
            }
        }

        if(!isSupported){
            logger.fatal("Unsupported platform [" + hardware.getPlatform().getName() + "]", new InitializationException("Engine", "Unsp"));
        }

        startApp();
    }

    public final int getFPS(){
        return fps;
    }

    protected abstract void init();

    protected abstract void onUpdate();
}
