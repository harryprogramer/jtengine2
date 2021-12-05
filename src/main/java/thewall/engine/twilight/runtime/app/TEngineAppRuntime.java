package thewall.engine.twilight.runtime.app;

import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import thewall.engine.twilight.TwilightApp;
import thewall.engine.twilight.display.GLFWWindowResizeSystem;
import thewall.engine.twilight.gui.imgui.ImmediateModeGUI;
import thewall.engine.twilight.gui.imgui.OnImmediateGUI;
import thewall.engine.twilight.gui.imgui.DearImmediateGUIMode;
import thewall.engine.twilight.hardware.SoundCard;
import thewall.engine.twilight.input.gamepad.GamepadLookupService;
import thewall.engine.twilight.renderer.MasterRenderer;
import thewall.engine.twilight.renderer.SyncTimer;
import thewall.engine.twilight.runtime.AbstractRuntime;
import thewall.engine.twilight.utils.WatchdogMonitor;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.*;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;


/**
 *
 * This environment runs the main engine program {@link TwilightApp}
 *
 * @deprecated api rebuild from 11.17.2021, {@link JTEEnvironment}
 */
@Deprecated(forRemoval = true)
public class TEngineAppRuntime extends AbstractRuntime<TwilightApp> {
    private int errorRepeatCount = 0;
    private Exception lastError = null;
    private final static Logger logger = LogManager.getLogger(TEngineAppRuntime.class);
    private final SyncTimer syncTimer = new SyncTimer(SyncTimer.LWJGL_GLFW);
    private final static ExecutorService scheduler = Executors.newSingleThreadExecutor();
    private volatile ImmediateModeGUI imGui;
    private TwilightApp twilightApp;
    volatile boolean isInit = false;
    private Thread runtimeThread = null;

    private WatchdogMonitor watchdogMonitor;

    volatile boolean isClosing = false;
    volatile boolean isClosed = false;

    private final List<Runnable> rendererTasks = new ArrayList<>();


    public TEngineAppRuntime() {
        super("Engine Thread");
    }

    @Override
    protected Thread getThread() {
        return runtimeThread;
    }

    @Override
    public void executeTask(Runnable runnable) {
        if(twilightApp == null){
            runnable.run();
        }else {
            rendererTasks.add(runnable);
        }
    }

    @Override
    protected void start(@NotNull TwilightApp program) {
        try {
            runtimeThread = Thread.currentThread();
            watchdogMonitor = new WatchdogMonitor(Thread.currentThread());
            watchdogMonitor.start();
            if (!program.getDebugConsole().isLogging())
                program.getDebugConsole().startLogging();
            //program.createDisplay();
            MasterRenderer masterRenderer = new MasterRenderer(program, program.getLoader());
            program.setWindowResizeSystem(new GLFWWindowResizeSystem(masterRenderer));
            program.setRenderer(masterRenderer);
            program.registerCallbacks();
            this.imGui = new DearImmediateGUIMode(program, "#version 400 core");
            this.imGui.init();
            program.setWatchdog(watchdogMonitor);
            program.setImmediateModeGUI(this.imGui);
            glfwFocusWindow(program.getWindow());
            program.showWindow();
            try {
                program.onEnable();
            }catch (Exception e){
                logger.fatal("Exception in app initialization function", e);
                forceStop();
            }
            logger.info("CPU:       " + program.getRealtimeHardware().getProcessor().getName());
            logger.info("OpenGL:    " + TwilightApp.getRenderAPIVersion());
            logger.info("GPU:       " + program.getRealtimeHardware().getUsedGraphic().getName());
            logger.info("Vendor:    " + program.getRealtimeHardware().getUsedGraphic().getVendor());
            logger.info("Memory :   " + (program.getRealtimeHardware().getMemory().getTotal()) / (1024L * 1024L) + "MB");
            logger.info("Baseboard: "  + program.getRealtimeHardware().getBaseboardManufacturer() + " " + program.getRealtimeHardware().getBaseboardModel());
            List<SoundCard> soundCards = program.getRealtimeHardware().getSoundCards();
            int i = 0;
            for(SoundCard soundCard : soundCards) {
                logger.info(String.format("Sound %d:   %s %s %s", ++i ,soundCard.getName(), soundCard.getCodec(), soundCard.getDriverVersion()));
            }
            GamepadLookupService gamepadLookupService = new GamepadLookupService();
            //glfwSetJoystickCallback(new GLFWJoystickCallback(program, gamepadLookupService));
            //program.setInput(new InputProvider(new GLFWKeyboard(program), new GLFWMouse(program), new GLFWGamepadManager(gamepadLookupService)));
            //program.setGuiRenderer(new GuiRenderer(program.getLoader()));
            this.twilightApp = program;
            logger.info("Initialization complete, runtime is ready");
            isInit = true;
            engineLoop();
        } catch (Throwable e){
            logger.fatal("An error has occurred while trying to start the engine core", e);
            forceStop();
        }
    }

    private @NotNull List<Integer> getActiveControllers(){
        int[] controllerDictionary = {GLFW_JOYSTICK_1, GLFW_JOYSTICK_2, GLFW_JOYSTICK_3,
                GLFW_JOYSTICK_4, GLFW_JOYSTICK_5, GLFW_JOYSTICK_6, GLFW_JOYSTICK_7,
                GLFW_JOYSTICK_8, GLFW_JOYSTICK_9, GLFW_JOYSTICK_10, GLFW_JOYSTICK_11,
                GLFW_JOYSTICK_12, GLFW_JOYSTICK_13, GLFW_JOYSTICK_14, GLFW_JOYSTICK_15,
                GLFW_JOYSTICK_16};

        List<Integer> controllers = new ArrayList<>();

        for(int i = 0; i < 15; i++){
            if(glfwJoystickPresent(controllerDictionary[i])){
                controllers.add(i + 1);
            }
        }

        return controllers;
    }

    @Override
    protected synchronized void stop() {
        if(!isClosed) {
            isClosing = true;
            logger.info("Shutting down [" + (twilightApp != null ? twilightApp.getName() : "N/A") + "] ...");
            stopEngine();
            if(twilightApp != null) {
                twilightApp.onDisable();
                twilightApp.getDebugConsole().closeConsole();
            }
            logger.info("Closing app...");
            isClosed = true;
            System.exit(-1);
        }
    }

    private void stopEngine(){
        if(twilightApp != null) {
            twilightApp.getRenderer().cleanUp();
            twilightApp.getLoader().cleanUp();
            if(twilightApp.isImmediateGUIHidden()){
                imGui.destroy();
            }
            glfwFreeCallbacks(twilightApp.getWindow());
            glfwDestroyWindow(twilightApp.getWindow());
        }

        glfwTerminate();
        try {
            Objects.requireNonNull(glfwSetErrorCallback(null)).free();
        }catch (Exception ignored){

        }
        scheduler.shutdown();
    }

    static boolean test = false;
    @SneakyThrows
    private void engineLoop(){

        while (!glfwWindowShouldClose(twilightApp.getWindow())) {
            try {
                if(isClosing){
                    break;
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
                long startTime = System.currentTimeMillis();
                if(twilightApp.isImmediateGUIHidden()){
                    imGui.renderBegin();
                    List<Method> callers = getMethodsAnnotatedWithIMGUI(twilightApp);
                    for(Method caller : callers){
                        caller.invoke(twilightApp);
                    }
                    imGui.renderEnd();
                }
                long endTime = System.currentTimeMillis();
                double finalTime = (endTime - startTime) / 1000.0;
                if(finalTime > 0.025){
                    logger.warn("ImGUI render is taking too long to render, ticks behind: " + finalTime);
                }

                twilightApp.updateDisplay();
                twilightApp.update();

                watchdogMonitor.keepAlive();

            }catch (Exception e){
                if(lastError != null){
                    if(e.getClass() == lastError.getClass()){

                        if(++errorRepeatCount % 100 == 0){
                            logger.warn(e.getClass().getSimpleName() + " is still throwing in the engine loop, more then " + errorRepeatCount + " times");
                        }
                        if(errorRepeatCount >= 500){
                            logger.fatal("Too much error for this same exception, shutting down...");
                            logger.fatal("Error stacktrace", e);
                            forceStop();
                        }
                    }
                }else {
                    logger.warn("Error while pulsing engine", e);
                }
                lastError = e;

            }
            syncTimer.sync(twilightApp.getFrameLimit());
        }

        stop();
    }

    private static @NotNull List<Method> getMethodsAnnotatedWithIMGUI(final @NotNull Object type) {
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

    @Override
    public boolean isReady() {
        return isInit;
    }

    private void scheduleTask(Runnable r){
        rendererTasks.add(r);
    }

}
