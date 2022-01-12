package jte2.engine.twilight.system.context.opengl.lwjgl;

import jte2.engine.twilight.Area;
import jte2.engine.twilight.audio.jmf.SoundManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.system.MemoryUtil;
import jte2.engine.twilight.assets.AssetManager;
import jte2.engine.twilight.assets.DesktopAssetManager;
import jte2.engine.twilight.audio.SoundMaster;
import jte2.engine.twilight.display.Display;
import jte2.engine.twilight.display.GLFWDisplay;
import jte2.engine.twilight.events.endpoints.EndpointHandler;
import jte2.engine.twilight.events.endpoints.StaticRouter;
import jte2.engine.twilight.events.EventManager;
import jte2.engine.twilight.events.JTEEventManager;
import jte2.engine.twilight.input.Input;
import jte2.engine.twilight.input.glfw.GLFWInput;
import jte2.engine.twilight.renderer.Renderer;
import jte2.engine.twilight.renderer.opengl.GL;
import jte2.engine.twilight.renderer.opengl.GL4;
import jte2.engine.twilight.renderer.opengl.GLRenderer;
import jte2.engine.twilight.renderer.opengl.lwjgl.LwjglGL;
import jte2.engine.twilight.renderer.opengl.vao.LegacyVAOManager;
import jte2.engine.twilight.system.AppSettings;
import jte2.engine.twilight.system.JTEContext;
import jte2.engine.twilight.texture.opengl.JTEGLTextureManager;

import java.nio.charset.StandardCharsets;

import static org.lwjgl.glfw.GLFW.*;


public class LegacyLwjglContext extends GLFWDisplay implements JTEContext {
    private final EndpointHandler endpointHandler = new StaticRouter();
    private final static Logger logger = LogManager.getLogger(LegacyLwjglContext.class);
    private final GL glContext = new LwjglGL();
    private final AppSettings appSettings;
    private final SoundMaster soundMaster;
    private final Renderer renderer;
    private final Input input;

    public LegacyLwjglContext(AppSettings appSettings){
        this.soundMaster = createBestSoundMaster(appSettings);
        logger.info("Context for [Sound Master]:        " + (this.soundMaster != null ? this.soundMaster.getClass().getName() : "No Context"));
        this.input = createBestInputManager(appSettings, this);
        logger.info("Context for [Input Manager]:       " + (this.input != null ? this.input.getClass().getName() : "No Context"));
        this.renderer = new GLRenderer (glContext, new LegacyVAOManager(glContext), new JTEGLTextureManager(glContext), this);
        logger.info("Context for [Renderer]:            " + this.renderer.getClass().getName());
        this.appSettings = appSettings;
    }

    public long getWindowHandle(){
        return getWindow();
    }

    @Override
    public AssetManager getAssetsManager() {
        return new DesktopAssetManager();
    }

    @Override
    public EndpointHandler getEndpointHandler() {
        return endpointHandler;
    }

    @Override
    public SoundMaster getSoundMaster() {
        return soundMaster;
    }

    @Override
    public Display getDisplay() {
        return this;
    }

    @Override
    public EventManager getEventManager() {
        return new JTEEventManager(); // TODO best event manager
    }

    @Override
    public Renderer getRenderer() {
        return renderer;
    }

    @Override
    public Input getInput() {
        return input;
    }

    public void createWindow(int width, int height, String name) {
        super.createDisplay(width, height, name);
    }

    @Override
    public boolean shouldClose() {
        return glfwWindowShouldClose(getWindow());
    }

    @Override
    public void update() {
        updateDisplay();

    }

    @Override
    public void init() {
        if(!glContext.isGL2Support()){
            logger.fatal("OpenGL 2.0+ is required to start");
            throw new UnsupportedOperationException("OpenGL 2.0+ unsupported");
        }else {
            logger.debug("OpenGL 2.0+ support available");
        }

        if(!glContext.isGL3Support()){
            logger.fatal("OpenGL 3.0+ is required to start");
            throw new UnsupportedOperationException("OpenGL 3.0+ unsupported");
        }else {
            logger.debug("OpenGL 3.0+ support available");
        }

        GLFWErrorCallback.createPrint(System.err).set();

        Area area = appSettings.getParam(AppSettings.SettingsType.DISPLAY_RESOLUTION, Area.class);

        area = area != null ? area : new Area(600, 600);

        createWindow(area.getWidth(), area.getHeight(), "");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_DOUBLEBUFFER, GLFW_TRUE);


        createCapacities();

        if(!glContext.isGL4Support()){
            logger.warn("OpenGL 4.0+ is not supported, some features may not be available");
        }else {
            logger.debug("OpenGL 4.0+ support available");
            logger.debug("Enabling OpenGL 4.0+ debug message callback");
            GL4 gl4 = (GL4) glContext;
            gl4.glEnable(GL4.GL_DEBUG_OUTPUT);
            gl4.glDebugMessageCallback((source, type, id, severity, length, message, userParam) -> {
                String msg = StandardCharsets.UTF_8.decode(MemoryUtil.memByteBuffer(message, length)).toString();
                String sourceMsg, typeMsg;
                switch (source){
                    case GL4.GL_DEBUG_SOURCE_API ->             sourceMsg = "SOURCE";
                    case GL4.GL_DEBUG_SOURCE_WINDOW_SYSTEM ->   sourceMsg = "WINDOW SYSTEM";
                    case GL4.GL_DEBUG_SOURCE_SHADER_COMPILER -> sourceMsg = "SHADER";
                    case GL4.GL_DEBUG_SOURCE_THIRD_PARTY ->     sourceMsg = "EXTERNAL";
                    case GL4.GL_DEBUG_SOURCE_APPLICATION ->     sourceMsg = "APPLICATION";
                    case GL4.GL_DEBUG_SOURCE_OTHER ->           sourceMsg = "OTHER";
                    default ->                                  sourceMsg = "UNKNOWN";
                }

                switch (type){
                    case GL4.GL_DEBUG_TYPE_ERROR ->                 typeMsg = "ERROR";
                    case GL4.GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR ->   typeMsg = "DEPRECATED";
                    case GL4.GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR ->    typeMsg = "UNDEFINED_BEHAVIOUR";
                    case GL4.GL_DEBUG_TYPE_PORTABILITY ->           typeMsg = "PORTABILITY";
                    case GL4.GL_DEBUG_TYPE_PERFORMANCE ->           typeMsg = "PERFORMANCE";
                    case GL4.GL_DEBUG_TYPE_OTHER ->                 typeMsg = "OTHER";
                    case GL4.GL_DEBUG_TYPE_MARKER ->                typeMsg = "MARKER";
                    default -> typeMsg = "UNKNOWN";
                }

                logger.warn(String.format("OpenGL %s [%s] [%d]: %s", typeMsg ,sourceMsg ,id, msg));
            }, MemoryUtil.NULL);
        }
    }

    @Override
    public void destroy() {
        glfwTerminate();
        GLFWErrorCallback callback = glfwSetErrorCallback(null);
        if(callback != null){
            callback.free();
        }
    }

    private void createCapacities() {
        glfwMakeContextCurrent(getWindow());
        org.lwjgl.opengl.GL.createCapabilities();
    }

    @SuppressWarnings("unchecked")
    private static @Nullable SoundMaster createBestSoundMaster(AppSettings appSettings){
        Class<? extends SoundMaster> clazz;
        try {
            Object object = appSettings.getSettingsMap().get(AppSettings.SettingsType.SOUND_RENDERER.getName());

            if(object == null){
                return new SoundManager(); // default
            }

            if(!(object instanceof Class)) {
                throw new Exception("(AppSettings) A different type of data was expected");
            }
            clazz = (Class<? extends SoundMaster>) appSettings.getSettingsMap().get(AppSettings.SettingsType.SOUND_RENDERER.getName());

            return clazz.getDeclaredConstructor().newInstance();
        }catch (Exception e){
            logger.fatal("Cannot create sound manager, ", e);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private static @Nullable Input createBestInputManager(AppSettings appSettings, LegacyLwjglContext context){
        Class<? extends Input> clazz;
        try {
            Object object = appSettings.getSettingsMap().get(AppSettings.SettingsType.INPUT_CONTEXT.getName());
            if(object == null){
                return new GLFWInput(context); // default
            }
            if(!(object instanceof Class)) {
                throw new Exception("(AppSettings) A different type of data was expected");
            }
            clazz = (Class<? extends Input>) appSettings.getSettingsMap().get(AppSettings.SettingsType.INPUT_CONTEXT.getName());
            return clazz.getDeclaredConstructor().newInstance();
        }catch (Exception e){
            logger.fatal("Cannot create input manager, ", e);
            return null;
        }
    }
}
