package thewall.game.blocks;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import thewall.engine.LegacyApp;
import thewall.engine.twilight.display.GLFWDisplayManager;
import thewall.engine.twilight.events.endpoints.Endpoint;
import thewall.engine.twilight.entity.Box;
import thewall.engine.twilight.entity.Camera;
import thewall.engine.twilight.gui.imgui.ImmediateModeGUI;
import thewall.engine.twilight.input.Input;
import thewall.engine.twilight.input.keyboard.KeyboardKeys;
import thewall.engine.twilight.input.mouse.CursorPosition;
import thewall.engine.twilight.material.Material;
import thewall.engine.twilight.utils.Colour;

public class Game extends LegacyApp {
    private final static Logger logger = LogManager.getLogger(Game.class);
    private Box[] boxes = new Box[100];

    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }

    @Override
    protected void init() {
        Camera camera = new Camera();
        camera.setTransformation(new Vector3f(0, 0, 20));
        viewPort.setCamera(camera);

        for(int i = 0; i < 1; i++){
            Box box = new Box(new Vector3f(0, 0, 0), 1, 1, 1);
            box.setTransformation(new Vector3f(50, 50, 50));
            box.setMaterial(new Material("Box Material").loadColour(Colour.AQUA));
            boxes[i] = box;
            rootNode.attachChild(box);
        }
    }

    private long previousTime = System.currentTimeMillis();
    private static final float speed = 0.05f;
    @Override
    protected void onUpdate() {
        long endTime = System.currentTimeMillis();
        if(endTime - previousTime > 500){
            previousTime = System.currentTimeMillis();
            logger.info("Cam: " + viewPort.getCamera().getTransformation().y);
            logger.info("Distance between [Box #1]: " + viewPort.getCamera().getTransformation().distance(boxes[0].getTransformation()));
        }

        //viewPort.getCamera().setRotation((float) input().getMouse().getCursorPosition().getXPos(), 0, 0);

        checkInputs();
        /*
        if(input().getKeyboard().isKeyPressed(KeyboardKeys.W_KEY)){
            viewPort.getCamera().getTransformation().add(0, 0, 1 * speed);
        }

        if(input().getKeyboard().isKeyPressed(KeyboardKeys.S_KEY)){
            viewPort.getCamera().getTransformation().add(0, 0, -1 * speed);
        }

        if(input().getKeyboard().isKeyPressed(KeyboardKeys.A_KEY)){
            viewPort.getCamera().getTransformation().add(-1  * speed, 0, 0);
        }

        if(input().getKeyboard().isKeyPressed(KeyboardKeys.D_KEY)){
            viewPort.getCamera().getTransformation().add(1 * speed, 0, 0);
        }

         */

    }

    private float moveSpeed = 0.2f;

    private double oldMouseX = 0;
    private double oldMouseY = 0;


    public void move(@NotNull Vector3f vector3f){
        float z = vector3f.z + viewPort.getCamera().getTransformation().z;
        float y = vector3f.y + viewPort.getCamera().getTransformation().y;

        viewPort.getCamera().setTransformation(vector3f.x + viewPort.getCamera().getTransformation().x, y, z);
    }

    public void move(float x, float y, float z){
        move(new Vector3f(x, y, z));
    }

    public void checkInputs(){
        Input input = input();
        CursorPosition pos = input.getMouse().getCursorPosition();
        double newMouseX = pos.getXPos();
        double newMouseY = pos.getYPos();

        Camera camera = viewPort.getCamera();
        float x = (float) Math.sin(Math.toRadians(camera.getRotation().y)) * (moveSpeed / 2 * GLFWDisplayManager.getFrameTimeSeconds());
        float z = (float) Math.cos(Math.toRadians(camera.getRotation().y)) * (moveSpeed / 2 * GLFWDisplayManager.getFrameTimeSeconds());


        if(input.getKeyboard().isKeyPressed(KeyboardKeys.A_KEY)){
            move(new Vector3f(-z, 0, x));
        }

        if(input.getKeyboard().isKeyPressed(KeyboardKeys.D_KEY)){
            move(new Vector3f(z, 0, -x));
        }

        if(input.getKeyboard().isKeyPressed(KeyboardKeys.W_KEY)){
            move(new Vector3f(-x, 0, -z));
        }

        if(input.getKeyboard().isKeyPressed(KeyboardKeys.S_KEY)){
            move(new Vector3f(x, 0, z));
        }

        if(input.getKeyboard().isKeyPressed(KeyboardKeys.SPACE_KEY)){
            move(0, moveSpeed / 2 * GLFWDisplayManager.getFrameTimeSeconds(), 0);
        }

        if(input.getKeyboard().isKeyPressed(KeyboardKeys.LEFT_SHIFT_KEY)){
            move(new Vector3f(0, -moveSpeed / 2 * GLFWDisplayManager.getFrameTimeSeconds(), 0));

        }

        if(input.getKeyboard().isKeyPressed(KeyboardKeys.LEFT_CONTROL_KEY)){
            moveSpeed = 0.6f;
        }else {
            moveSpeed = 0.2f;
        }

        float dx = (float) (newMouseX - oldMouseX);
        float dy = (float) (newMouseY - oldMouseY);


        Vector3f finalRotation = camera.getRotation();
        float mouseSensitivity = 0.15f;
        finalRotation.add(new Vector3f(-dy * mouseSensitivity, -dx * mouseSensitivity, 0));

        if(finalRotation.x >= 90){
            finalRotation.x = 90f;
        }else if(finalRotation.x < -90){
            finalRotation.x = -90f;
        }

        camera.getRotation().set(finalRotation.x, finalRotation.y, finalRotation.z);
        oldMouseX = newMouseX;
        oldMouseY = newMouseY;
    }

    boolean isInit = false;

    @Endpoint(route = ImmediateModeGUI.GUI_ENDPOINT)
    public void onGui(){
        if(!isInit){
            if(GLFWDisplayManager.pointer() == 0){
                return;
            }
            ImGui.createContext();
            ImGuiIO io = ImGui.getIO();
            io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);
            imGuiGlfw.init(GLFWDisplayManager.pointer(), true);
            imGuiGl3.init("#version 400 core");
            isInit = true;
        }
        imGuiGlfw.newFrame();
        ImGui.newFrame();

        ImGui.begin("Console");
        ImGui.text("siema siema o tje porzeka zkazudkamsdhid czxmcjd jd");
        ImGui.end();

        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());

        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final long backupWindowPtr = org.lwjgl.glfw.GLFW.glfwGetCurrentContext();
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
            GLFW.glfwMakeContextCurrent(backupWindowPtr);
        }
    }
}
