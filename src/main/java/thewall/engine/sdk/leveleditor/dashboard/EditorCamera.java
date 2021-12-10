package thewall.engine.sdk.leveleditor.dashboard;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import thewall.engine.twilight.Application;
import thewall.engine.twilight.display.GLFWDisplayManager;
import thewall.engine.twilight.spatials.Camera;
import thewall.engine.twilight.input.Input;
import thewall.engine.twilight.input.keyboard.KeyboardKeys;
import thewall.engine.twilight.input.mouse.CursorPosition;

public class EditorCamera extends Camera {
    private final Application app;

    private float moveSpeed = 0.2f;
    private double oldMouseX = 0;
    private double oldMouseY = 0;

    private boolean isCamera = true;

    public EditorCamera(Application app){
        this.app = app;
    }


    public void setCamera(boolean status){
        this.isCamera = status;
    }

    public void update(){
            Input input = app.getInput();
            CursorPosition pos = input.getMouse().getCursorPosition();
            double newMouseX = pos.getXPos();
            double newMouseY = pos.getYPos();

            Camera camera = app.getViewPort().getCamera();
            float x = (float) Math.sin(Math.toRadians(camera.getRotation().y)) * (moveSpeed / 2 * GLFWDisplayManager.getFrameTimeSeconds());
            float z = (float) Math.cos(Math.toRadians(camera.getRotation().y)) * (moveSpeed / 2 * GLFWDisplayManager.getFrameTimeSeconds());


            if (input.getKeyboard().isKeyPressed(KeyboardKeys.A_KEY)) {
                move(new Vector3f(-z, 0, x));
            }

            if (input.getKeyboard().isKeyPressed(KeyboardKeys.D_KEY)) {
                move(new Vector3f(z, 0, -x));
            }

            if (input.getKeyboard().isKeyPressed(KeyboardKeys.W_KEY)) {
                move(new Vector3f(-x, 0, -z));
            }

            if (input.getKeyboard().isKeyPressed(KeyboardKeys.S_KEY)) {
                move(new Vector3f(x, 0, z));
            }

            if (input.getKeyboard().isKeyPressed(KeyboardKeys.SPACE_KEY)) {
                move(0, moveSpeed / 2 * GLFWDisplayManager.getFrameTimeSeconds(), 0);
            }

            if (input.getKeyboard().isKeyPressed(KeyboardKeys.LEFT_SHIFT_KEY)) {
                move(new Vector3f(0, -moveSpeed / 2 * GLFWDisplayManager.getFrameTimeSeconds(), 0));

            }

            if (input.getKeyboard().isKeyPressed(KeyboardKeys.LEFT_CONTROL_KEY)) {
                moveSpeed = 0.6f;
            } else {
                moveSpeed = 0.2f;
            }

            float dx = (float) (newMouseX - oldMouseX);
            float dy = (float) (newMouseY - oldMouseY);


            Vector3f finalRotation = camera.getRotation();
            float mouseSensitivity = 0.15f;
            finalRotation.add(new Vector3f(-dy * mouseSensitivity, -dx * mouseSensitivity, 0));

            if (finalRotation.x >= 90) {
                finalRotation.x = 90f;
            } else if (finalRotation.x < -90) {
                finalRotation.x = -90f;
            }

            camera.getRotation().set(finalRotation.x, finalRotation.y, finalRotation.z);
            oldMouseX = newMouseX;
            oldMouseY = newMouseY;
    }

    public void move(float x, float y, float z){
        move(new Vector3f(x, y, z));
    }

    public void move(@NotNull Vector3f vector3f){
        float z = vector3f.z + app.getViewPort().getCamera().getTransformation().z;
        float y = vector3f.y + app.getViewPort().getCamera().getTransformation().y;

        app.getViewPort().getCamera().setTransformation(vector3f.x + app.getViewPort().getCamera().getTransformation().x, y, z);
    }
}
