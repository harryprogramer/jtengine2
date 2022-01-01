package thewall.engine.sdk.leveleditor;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector3f;
import thewall.engine.LegacyApp;
import thewall.engine.sdk.leveleditor.dashboard.AWTConsole;
import thewall.engine.sdk.leveleditor.dashboard.EditorCamera;
import thewall.engine.sdk.leveleditor.dashboard.SpatialService;
import thewall.engine.sdk.leveleditor.dashboard.commands.*;
import thewall.engine.sdk.leveleditor.input.KeyboardInputCallback;
import thewall.engine.sdk.leveleditor.net.HTTPUpdate;
import thewall.engine.sdk.leveleditor.net.UpdateData;
import thewall.engine.sdk.leveleditor.net.UpdateException;
import thewall.engine.sdk.leveleditor.net.UpdateManager;
import thewall.engine.twilight.material.Colour;
import thewall.engine.twilight.networking.ConnectionRefusedException;
import thewall.engine.twilight.spatials.Box;
import thewall.engine.twilight.spatials.Light;
import thewall.engine.twilight.spatials.Spatial;
import thewall.engine.twilight.system.AppSettings;
import thewall.engine.twilight.system.NativeContext;
import thewall.engine.twilight.system.context.opengl.lwjgl.LegacyLwjglContext;

@NativeContext(context = LegacyLwjglContext.class)
public class Editor extends LegacyApp {
    private final static Logger logger = LogManager.getLogger(Editor.class);
    private final SpatialService spatialService = new SpatialService();
    private final UpdateManager updateManager = new HTTPUpdate(this);
    private final static String VERSION = "JTEEditor 1.2";
    private final static int VERSION_NUMBER = 110;
    private final AWTConsole console;
    private EditorCamera camera;

    private boolean isCamera = true;

    public Editor(){
        console = new AWTConsole();
    }

    @Override
    protected void init() {
        initArgs();

        camera = new EditorCamera(this);
        getInput().getKeyboard().setKeyboardCallback(new KeyboardInputCallback(this));
        getInput().getMouse().setCursorPosition(0, 0);
        getInput().getMouse().disableCursor();
        setFrameLimit(190);
        getDisplay().setVSync(false);
        getViewPort().addLight(new Light(new Vector3f(0, 5000, 0), Colour.WHITE, new Vector3f(0.000001f, 0.000001f, 0.000001f)));
        //setShader(new PreviewLightShader());
        Spatial spatial = new Box(2, 2, 2);
        //spatial.getMaterial().setShader(new StaticShader());
        spatial.setScale(25);
        spatial.getMaterial().setTexture(getAssetsManager().loadTexture("pob_vafor_em_epica.png"));
        spatial.getMaterial().setColour(Colour.RED);
        //rootNode.attachChild(spatial);

    }

    private long previousTime = System.currentTimeMillis();
    @Override
    protected void onUpdate() {
        if(isCamera) {
            camera.update();
        }
        long endTime = System.currentTimeMillis();
        if(endTime - previousTime > 1000){
            previousTime = System.currentTimeMillis();
        }

        Vector3f camera = getViewPort().getCamera().getTransformation();
        getDisplay().setTitle(String.format("JTEEditor Preview 1.2 | FPS: %d | X: %.0f | Y: %.0f | Z: %.0f | LOOKING AT: %f",
                getFPS(), camera.x, camera.y, camera.z,
                getViewPort().getCamera().getRotation().y));

    }

    @Override
    public void onExit() {
        console.disableDashboard();
    }

    public void setCamera(boolean status){
        this.isCamera = status;
    }

    private void initArgs(){
        TeleportArgument moveArgument = new TeleportArgument(this);
        console.registerArg("box", new BoxArgument(this, spatialService));
        console.registerArg("material", new MaterialArgument(spatialService, getAssetsManager()));
        console.registerArg("destroy", new DestroyArgument(this, spatialService));
        console.registerArg("move", new MoveArgument(this, spatialService));
        console.registerArg("shader", new ShaderArgument(this));
        console.registerArg("scale", new ScaleArgument(spatialService));
        console.registerArg("tp", moveArgument);
        console.registerArg("teleport", moveArgument);
        console.registerArg("lmdl", new ModelCommand(this, spatialService, getAssetsManager()));
    }

    public static String getVersion(){
        return VERSION;
    }

    public static int getVersionNumber() {
        return VERSION_NUMBER;
    }

    public static void main(String[] args) {
        Editor editor = new Editor();
        UpdateData data;
        try {
            data = editor.updateManager.checkLatestVersion();
            if(data.getVersionNumber() > Editor.getVersionNumber()){
                logger.info("New update failed [{}]", data.getName());
                editor.updateManager.updateVersion(data.getVersion());
                logger.info("Restarting editor...");
                System.exit(0);
            }else {
                logger.info("Good news! Editor is up to date [" + Editor.getVersion() + "]");
            }
        } catch (ConnectionRefusedException | UpdateException e) {
            logger.warn("Cannot check for new version", e);
        }
        AppSettings appSettings = new AppSettings();
        appSettings.setTitle("JTEEditor Preview 1.41");
        editor.setSettings(appSettings);
        editor.start();
        editor.console.startDashboard();
    }
}

