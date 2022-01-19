package jte2.engine.sdk.leveleditor;


import jte2.engine.LegacyApp;
import jte2.engine.sdk.leveleditor.dashboard.AWTConsole;
import jte2.engine.sdk.leveleditor.dashboard.EditorCamera;
import jte2.engine.sdk.leveleditor.dashboard.SpatialService;
import jte2.engine.sdk.leveleditor.dashboard.commands.*;
import jte2.engine.sdk.leveleditor.dashboard.commands.models.CreateModelArgument;
import jte2.engine.sdk.leveleditor.dashboard.commands.models.SaveModelArgument;
import jte2.engine.sdk.leveleditor.input.KeyboardInputCallback;
import jte2.engine.sdk.leveleditor.net.HTTPUpdate;
import jte2.engine.sdk.leveleditor.net.UpdateData;
import jte2.engine.sdk.leveleditor.net.UpdateException;
import jte2.engine.sdk.leveleditor.net.UpdateManager;
import jte2.engine.twilight.material.Colour;
import jte2.engine.twilight.networking.ConnectionRefusedException;
import jte2.engine.twilight.spatials.Box;
import jte2.engine.twilight.spatials.Light;
import jte2.engine.twilight.spatials.Spatial;
import jte2.engine.twilight.system.NativeContext;
import jte2.engine.twilight.system.context.opengl.lwjgl.LegacyLwjglContext;
import jte2.engine.twilight.viewport.Node;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector3f;

@NativeContext(context = LegacyLwjglContext.class)
public class Editor extends LegacyApp {
    private final static Logger logger = LogManager.getLogger(Editor.class);
    private final SpatialService spatialService = new SpatialService();
    private final UpdateManager updateManager = new HTTPUpdate(this);
    private final static String VERSION = "JTEEditor 1.3.4";
    private final static int VERSION_NUMBER = 134;
    private final AWTConsole console;
    private Node currentScene;
    private EditorCamera camera;
    private Light light;

    private boolean isCamera = true;

    public Editor(){
        console = new AWTConsole();
        Thread.currentThread().setName("JTEditorThread");
    }

    @Override
    protected void init() {
        initArgs();

        getDisplay().setTitle("JTEEditor Preview 1.41");

        camera = new EditorCamera(this);
        light = new Light(camera.getTransformation(), Colour.WHITE, new Vector3f(0.1f, 0.1f, 0.1f));
        viewPort.addLight(light);
        getInput().getKeyboard().setKeyboardCallback(new KeyboardInputCallback(this));
        getInput().getMouse().setCursorPosition(0, 0);
        getInput().getMouse().disableCursor();
        setFrameLimit(190);
        getDisplay().setVSync(false);
        Spatial spatial = new Box(2, 2, 2);
        spatial.setScale(25);
        spatial.getMaterial().setTexture(getAssetsManager().loadTexture("pob_vafor_em_epica.png"));
        spatial.getMaterial().setColour(Colour.RED);
        viewPort.detachScene(rootNode);

        updateCheck();

        getSound().playBackground(0.009f, 1f, "res/music/ambient/void.wav");
    }

    public void setScene(Node node){
        this.viewPort.detachScene(currentScene);
        if(node != null) {
            this.viewPort.attachScene(node);
            this.currentScene = node;
        }else {
            this.viewPort.attachScene(new Node());
            this.currentScene = null;
        }
    }

    public Node getScene(){
        return currentScene;
    }

    private long previousTime = System.currentTimeMillis();
    @Override
    protected void onUpdate() {
        if(isCamera) {
            viewPort.getLight(0).getPosition().set(viewPort.getCamera().getTransformation());
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
        console.registerArg("newmdl", new CreateModelArgument(this));
        console.registerArg("save", new SaveModelArgument(this));
        console.registerArg("bt", new BrightnessArgument(this));
    }

    public static String getEditorVersion(){
        return VERSION;
    }

    public static int getVersionNumber() {
        return VERSION_NUMBER;
    }

    public void updateCheck(){
        UpdateData data;
        try {
            data = updateManager.checkLatestVersion();
            if(data.getVersionNumber() > Editor.getVersionNumber()){
                logger.info("New update found [{}]", data.getName());
                updateManager.updateVersion(data.getVersion());
                logger.info("Restarting editor...");
                System.exit(15);
            }else {
                logger.info("Good news! Editor is up to date [" + Editor.getEditorVersion() + "]");
            }
        } catch (ConnectionRefusedException | UpdateException e) {
            logger.warn("Cannot check updates", e);
        }
    }

    public static void main(String[] args) {
        Editor editor = new Editor();
        editor.start();
        editor.console.startDashboard();
    }
}

