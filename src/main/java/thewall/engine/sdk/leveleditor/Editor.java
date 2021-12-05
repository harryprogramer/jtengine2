package thewall.engine.sdk.leveleditor;


import org.joml.Vector3f;
import thewall.engine.LegacyApp;
import thewall.engine.sdk.leveleditor.dashboard.AWTDashboard;
import thewall.engine.sdk.leveleditor.dashboard.EditorCamera;
import thewall.engine.sdk.leveleditor.dashboard.SpatialService;
import thewall.engine.sdk.leveleditor.dashboard.commands.BoxArgument;
import thewall.engine.sdk.leveleditor.dashboard.commands.DestroyArgument;
import thewall.engine.sdk.leveleditor.dashboard.commands.MaterialArgument;
import thewall.engine.sdk.leveleditor.dashboard.commands.MoveArgument;
import thewall.engine.twilight.entity.Spatial;
import thewall.engine.twilight.input.keyboard.KeyboardKeys;
import thewall.engine.twilight.input.mouse.CursorPosition;
import thewall.engine.twilight.system.AppSettings;

public class Editor extends LegacyApp {
    private final SpatialService spatialService = new SpatialService();
    private AWTDashboard console;
    private EditorCamera camera;

    public Editor(){
        console = new AWTDashboard();
    }

    @Override
    protected void init() {
        camera = new EditorCamera(this);
    }

    @Override
    protected void onUpdate() {
        if(input().getMouse().isCursorDisabled()) {
            camera.update();
        }
    }

    private void initArgs(){
        console.registerArg("box", new BoxArgument(this, spatialService));
        console.registerArg("material", new MaterialArgument(spatialService));
        console.registerArg("destroy", new DestroyArgument(this, spatialService));
        console.registerArg("move", new MoveArgument(this, spatialService));
    }

    private static float moveSensitivity = 0.01f;
    public void moveSpatialMouseX(Spatial spatial){
        CursorPosition calibrateValue = input().getMouse().getCursorPosition();
        double calibrateX = calibrateValue.getXPos();
        Vector3f spatialVector = spatial.getTransformation();
        while (!input().getKeyboard().isKeyPressed(KeyboardKeys.TAB_KEY)){
            CursorPosition position = input().getMouse().getCursorPosition();
            double x = position.getXPos();
            spatial.setTransformation((float) (calibrateX - x) * moveSensitivity, spatialVector.y, spatialVector.z);
        }
    }


    public static void main(String[] args) {
        Editor editor = new Editor();
        editor.initArgs();
        editor.console.startDashboard();
        AppSettings appSettings = new AppSettings();
        appSettings.setTitle("JTEEditor Preview 1.41");
        editor.setSettings(appSettings);
        editor.start();
    }
}

