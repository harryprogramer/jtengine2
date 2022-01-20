package jte2.engine.twilight.runtime.app;

import jte2.engine.twilight.texture.Texture;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2f;
import jte2.engine.twilight.Application;
import jte2.engine.twilight.gui.GUIImage;
import jte2.engine.twilight.shaders.ShaderHandle;
import jte2.engine.twilight.viewport.*;
import jte2.engine.twilight.debugger.console.DebugConsole;
import jte2.engine.twilight.events.endpoints.EndpointHandler;
import jte2.engine.twilight.gui.imgui.ImmediateModeGUI;
import jte2.engine.twilight.hardware.Hardware;
import jte2.engine.twilight.renderer.Renderer;
import jte2.engine.twilight.renderer.SyncTimer;
import jte2.engine.twilight.runtime.AbstractRuntime;
import jte2.engine.twilight.system.AppSettings;
import jte2.engine.twilight.system.JTEContext;
import jte2.engine.twilight.system.JTESystem;
import jte2.engine.twilight.material.Colour;
import jte2.engine.twilight.system.NativeContext;
import jte2.engine.twilight.utils.Validation;
import jte2.engine.twilight.utils.WatchdogMonitor;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * New runtime for Twilight Engine apps
 * <p>Program: {@link Application}
 */
public final class JTEEnvironment extends AbstractRuntime<Application> {
    private final static Logger logger = LogManager.getLogger(JTEEnvironment.class);
    private final static SyncTimer syncTimer = new SyncTimer(SyncTimer.LWJGL_GLFW);
    private final List<Runnable> rendererTasks = new ArrayList<>();
    private final AtomicBoolean shouldClose = new AtomicBoolean(false);
    private final AtomicBoolean isReady = new AtomicBoolean(false);
    private EndpointHandler endpointHandler;
    private WatchdogMonitor watchdog;
    private Renderer renderer;
    private JTEContext context;
    private Thread environmentThread;
    private boolean isClosing = false;
    private Application app;

    private boolean isImGUI = false;

    public JTEEnvironment() {
        super("JTE Environment");
    }

    private void createBestContext(AppSettings settings, Hardware hardware){
        try {
            this.context = JTESystem.findBestContext(settings, hardware);
        } catch (Throwable e) {
            logger.fatal("Fatal error while creating context for app, aborting");
            throw e;
        }
    }

    @Override
    protected Thread getThread() {
        return environmentThread;
    }

    @Override
    public void executeTask(Runnable runnable) {
        if(!isReady.get()){
            runnable.run();
        }
        rendererTasks.add(runnable);
    }

    @Override
    protected void start(Application program) {
        Validation.checkNull(program);
        logger.info("Capturing runtime thread...");
        this.environmentThread = Thread.currentThread();
        this.app = program;
        watchdog = new WatchdogMonitor(environmentThread);
        watchdog.start();

        logger.info("Java VM:                   " + System.getProperty("java.vm.name"));
        logger.info("Java Home:                 " + System.getProperty("java.home"));
        logger.info("Java Vendor:               " + System.getProperty("java.vendor"));
        logger.info("Java Version:              " + System.getProperty("java.version"));
        logger.info("Java Specification Vendor: " + System.getProperty("java.specification.vendor"));


        logger.info("Finding best context for runtime");

        Class<?> clazz = program.getClass();
        if(clazz.isAnnotationPresent(NativeContext.class)){
            NativeContext nativeContext = clazz.getAnnotation(NativeContext.class);
            Class<? extends JTEContext> contextClazz = nativeContext.context();
            try {
                Constructor<? extends JTEContext> constructor = null;

                try{
                    constructor = contextClazz.getDeclaredConstructor(AppSettings.class);
                    this.context = constructor.newInstance(program.getSettings());
                }finally {
                    if(constructor == null){
                        try{
                            this.context = contextClazz.getDeclaredConstructor().newInstance();
                        }catch (Throwable t){
                            logger.error(String.format("Cannot create native context [%s]", contextClazz.getName()), t);
                            createBestContext(program.getSettings(), program.getHardware());
                        }
                    }
                }
            } catch (Exception e) {
                logger.error(String.format("Cannot create native context [%s]", contextClazz.getName()), e);
                createBestContext(program.getSettings(), program.getHardware());
            }
        }else {
            createBestContext(program.getSettings(), program.getHardware());
        }

        context.init();

        logger.info("Available processors to the JVM: {}", Runtime.getRuntime().availableProcessors());
        logger.info("CPU:                       " + app.getHardware().getProcessor().getName());
        logger.info("Renderer:                  " + context.getRenderer().getName());
        logger.info("GPU:                       " + app.getHardware().getUsedGraphic().getName());
        logger.info("Vendor:                    " + app.getHardware().getUsedGraphic().getVendor());
        logger.info("Memory :                   " + (app.getHardware().getMemory().getTotal()) / (1024L * 1024L) + "MB");
        logger.info("Baseboard:                 "  + app.getHardware().getBaseboardManufacturer() + " " + app.getHardware().getBaseboardModel());

        logger.info("Context found! [" + context.getClass().getName() + "]");

        initAppCapacities();

        renderer.init(app.getViewPort());

        //displayIntro();

        app.onInit();

        endpointHandler = context.getEndpointHandler();

        renderer.setBackground(new Colour(57, 135, 145));
        //renderer.setBackground(Colour.WHITE);

        isReady.set(true);

        endpointHandler.findRoutes(app);

        isImGUI = endpointHandler.getRoutes(ImmediateModeGUI.GUI_ENDPOINT) != null;


        renderer.setBackground(Colour.AQUA);
        renderer.showSkybox();

        if(app.getSettings().getParam(AppSettings.SettingsType.AUTO_FOCUS, Boolean.class))
            context.getDisplay().focus();

        loop();
    }

    private void displayIntro(){
        app.getGUIViewPort().getRenderQueue().clear();

        long startTime = System.currentTimeMillis();
        Texture texture = context.getAssetsManager().loadTexture("nvidia-logo.png");
        GUIImage image = new GUIImage(texture, new Vector2f(0, 0), 0.85f, 0.85f);
        Node2D node2D = new Node2D();
        node2D.attachChild(image);
        app.getGUIViewPort().attachScene(node2D);
        renderer.hideSkybox();
        renderer.setBackground(new Colour(255, 255, 255));
        renderer.prepareRenderQueue(app.getViewPort().getRenderQueue(), app.getGUIViewPort().getRenderQueue());
        while (true){
            long endTime = System.currentTimeMillis();
            if(endTime - startTime > 1500){
                break;
            }
            context.update();
            renderer.render(app.getViewPort(), app.getGUIViewPort());
        }

        app.getGUIViewPort().getRenderQueue().clear();
    }

    private void initAppCapacities(){
        this.app.setEventManager(context.getEventManager());
        this.app.setInput(context.getInput());
        this.app.setSound(context.getSoundMaster());
        this.app.setDisplay(context.getDisplay());
        this.app.setAssetsManager(context.getAssetsManager());
        this.renderer = context.getRenderer();
    }
    @Override
    protected void stop() {
        if(isClosing){
            throw new RuntimeException("Environment is already closing. Stand by.");
        }
        logger.info("Closing runtime for [" + app + "]");
        shouldClose.set(true);
        isClosing = true;

        logger.info("Stopping JTE runtime watchdog");
        watchdog.stop();
        app.onClose();
        context.getSoundMaster().stopMaster();
        DebugConsole.getConsole().closeConsole();
    }

    @Override
    public boolean isReady() {
        return isReady.get();
    }

    private void loop(){
        while (!shouldClose.get()){
            try {
                if(context.shouldClose()){
                    shouldClose.set(true);
                    continue;
                }

                if (!rendererTasks.isEmpty()) {
                    for (Iterator<Runnable> it = rendererTasks.iterator(); it.hasNext(); ) {
                        Runnable task = it.next();
                        try {
                            task.run();
                        } catch (Exception e) {
                            logger.warn("Render task error", e);
                        }
                        it.remove();
                    }
                }

                context.update();

                app.update();

                ViewPort worldViewport = app.getViewPort();
                ViewPort2D viewport2D = app.getGUIViewPort();
                RenderQueue renderQueue = worldViewport.getRenderQueue();
                RenderQueue2D queue2D = viewport2D.getRenderQueue();
                ShaderHandle shaderHandle = app.getShader();
                if(shaderHandle != null) {
                    //renderer.setSpatialShader(shaderHandle);
                }
                renderer.prepareRenderQueue(renderQueue, queue2D);

                if(isImGUI) { // TODO: move to renderer
                    endpointHandler.callEndpoint(ImmediateModeGUI.GUI_ENDPOINT);
                }
                renderer.render(worldViewport, viewport2D);

                watchdog.keepAlive();

                syncTimer.sync(app.getFrameLimit());
            }catch (Throwable t){
                logger.error("Error while pulsing engine", t);
                break;
            }
        }

        cleanUp();
        if(!isClosing) {
            forceStop();
        }
    }


    private void cleanUp(){
        renderer.cleanUp();
        context.destroy();;
    }
}
