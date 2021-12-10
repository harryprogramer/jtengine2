package thewall.engine.twilight.runtime.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thewall.engine.twilight.Application;
import thewall.engine.twilight.RenderQueue;
import thewall.engine.twilight.ViewPort;
import thewall.engine.twilight.debugger.console.DebugConsole;
import thewall.engine.twilight.events.endpoints.EndpointHandler;
import thewall.engine.twilight.gui.imgui.ImmediateModeGUI;
import thewall.engine.twilight.hardware.SoundCard;
import thewall.engine.twilight.renderer.Renderer;
import thewall.engine.twilight.renderer.SyncTimer;
import thewall.engine.twilight.runtime.AbstractRuntime;
import thewall.engine.twilight.system.JTEContext;
import thewall.engine.twilight.system.JTESystem;
import thewall.engine.twilight.material.Colour;
import thewall.engine.twilight.utils.Validation;
import thewall.engine.twilight.utils.WatchdogMonitor;

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

        try {
            this.context = JTESystem.findBestContext(program.getSettings(), program.getHardware());
        }catch (Throwable e){
            logger.fatal("Fatal error while creating context for app, aborting");
            throw e;
        }

        context.init();

        logger.info("CPU:                       " + app.getHardware().getProcessor().getName());
        logger.info("Renderer:                  " + context.getRenderer().getName());
        logger.info("GPU:                       " + app.getHardware().getUsedGraphic().getName());
        logger.info("Vendor:                    " + app.getHardware().getUsedGraphic().getVendor());
        logger.info("Memory :                   " + (app.getHardware().getMemory().getTotal()) / (1024L * 1024L) + "MB");
        logger.info("Baseboard:                 "  + app.getHardware().getBaseboardManufacturer() + " " + app.getHardware().getBaseboardModel());
        List<SoundCard> soundCards = app.getHardware().getSoundCards();
        int i = 0;
        for(SoundCard soundCard : soundCards) {
            logger.info(String.format("Sound %d:   %s %s %s", ++i ,soundCard.getName(), soundCard.getCodec(), soundCard.getDriverVersion()));
        }
        logger.info("Context found! [" + context.getClass().getName() + "]");

        initAppCapacities();

        app.onInit();

        renderer.init(app.getViewPort());

        endpointHandler = context.getEndpointHandler();

        renderer.setBackground(new Colour(57, 135, 145));
        //renderer.setBackground(Colour.WHITE);

        isReady.set(true);

        endpointHandler.findRoutes(app);

        isImGUI = endpointHandler.getRoutes(ImmediateModeGUI.GUI_ENDPOINT) != null;

        loop();
    }

    private void initAppCapacities(){
        this.app.setEventManager(context.getEventManager());
        this.app.setInput(context.getInput());
        this.app.setSound(context.getSoundMaster());
        this.app.setDisplay(context.getDisplay());
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
                RenderQueue renderQueue = worldViewport.getRenderQueue();

                renderer.prepareRenderQueue(renderQueue);

                if(isImGUI) { // TODO: move to renderer
                    endpointHandler.callEndpoint(ImmediateModeGUI.GUI_ENDPOINT);
                }
                renderer.render(worldViewport);

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
