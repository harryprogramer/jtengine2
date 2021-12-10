package thewall.engine.sdk.leveleditor;


import org.joml.Vector3f;
import thewall.engine.LegacyApp;
import thewall.engine.sdk.leveleditor.dashboard.AWTConsole;
import thewall.engine.sdk.leveleditor.dashboard.EditorCamera;
import thewall.engine.sdk.leveleditor.dashboard.SpatialService;
import thewall.engine.sdk.leveleditor.dashboard.commands.*;
import thewall.engine.sdk.leveleditor.input.KeyboardInputCallback;
import thewall.engine.twilight.material.Colour;
import thewall.engine.twilight.spatials.Light;
import thewall.engine.twilight.system.AppSettings;

public class Editor extends LegacyApp {
    private final SpatialService spatialService = new SpatialService();
    private final AWTConsole console;
    private EditorCamera camera;

    private boolean isCamera = true;

    public Editor(){
        console = new AWTConsole();
    }

    @Override
    protected void init() {
        camera = new EditorCamera(this);
        getInput().getKeyboard().setKeyboardCallback(new KeyboardInputCallback(this));
        getInput().getMouse().setCursorPosition(0, 0);
        getInput().getMouse().disableCursor();
        setFrameLimit(190);
        getDisplay().setVSync(false);
        getViewPort().addLight(new Light(new Vector3f(0, 5000, 0), Colour.WHITE, new Vector3f(0.000001f, 0.000001f, 0.000001f)));
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
        getDisplay().setTitle(String.format("JTEEditor Preview 1.41 | FPS: %d | X: %.0f | Y: %.0f | Z: %.0f | LOOKING AT: %f",
                getFPS(), camera.x, camera.y, camera.z,
                getViewPort().getCamera().getRotation().x));

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
        console.registerArg("material", new MaterialArgument(spatialService));
        console.registerArg("destroy", new DestroyArgument(this, spatialService));
        console.registerArg("move", new MoveArgument(this, spatialService));
        console.registerArg("tp", moveArgument);
        console.registerArg("teleport", moveArgument);
    }


    public static void main(String[] args) {
        Editor editor = new Editor();
        editor.initArgs();
        AppSettings appSettings = new AppSettings();
        appSettings.setTitle("JTEEditor Preview 1.41");
        editor.setSettings(appSettings);
        editor.start();
        editor.console.startDashboard();
    }
}

