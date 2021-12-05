package thewall.engine.twilight.entity;

import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector3f;
import thewall.engine.twilight.display.GLFWDisplayManager;
import thewall.engine.twilight.input.Input;
import thewall.engine.twilight.input.mouse.CursorPosition;
import thewall.engine.twilight.models.TexturedModel;
import thewall.game.thewall.Game;

@Deprecated(forRemoval = true)
public class Player extends Spatial {
    private final static Logger logger = LogManager.getLogger(Player.class);

    private static final float RUN_SPEED = 20;
    private static final float TURN_SPEED = 160;
    private static final float GRAVITY = -50;
    private static final float JUMP_POWER = 30;


    private static final float TERRAIN_HEIGHT = 0;
    boolean isOnAir = false;

    private float moveSpeed = 0.2f;

    private float upwardsSpeed = 0;

    private double oldMouseX = 0;
    private double oldMouseY = 0;

    @Getter
    private final Camera camera;

    public Player(TexturedModel texturedModel, Vector3f vector3f, float rotX, float rotY, float rotZ, float scale) {
        // FIXME super(texturedModel, vector3f, scale, terrain);
        this.camera = new Camera(vector3f);
        camera.setTransformation(vector3f.x - 25, vector3f.y + 25, vector3f.z);
        getRotation().set(0, 180, 0);
        camera.getRotation().set(0, 180, 0);
    }

    public void tick(){
        if(Game.getGame().input().getMouse().isCursorDisabled()) {
            checkInputs();
        }
    }


    private void jump(){
        if(!isOnAir) {
            this.upwardsSpeed = JUMP_POWER;
            isOnAir = true;
        }
    }

    public void checkInputs(){
        Input input = Game.getGame().input();
        CursorPosition pos = input.getMouse().getCursorPosition();
        double newMouseX = pos.getXPos();
        double newMouseY = pos.getYPos();

        float x = (float) Math.sin(Math.toRadians(camera.getRotation().y)) * (moveSpeed / 2 * GLFWDisplayManager.getFrameTimeSeconds());
        float z = (float) Math.cos(Math.toRadians(camera.getRotation().y)) * (moveSpeed / 2 * GLFWDisplayManager.getFrameTimeSeconds());

        /*
        if(input.getKeyboard().isKeyPressed(KeyboardKeys.A_KEY)){
            move(new Vector3f(-z, 0, x));
            camera.move(new Vector3f(-z, 0, x));

        }

        if(input.getKeyboard().isKeyPressed(KeyboardKeys.D_KEY)){
            move(new Vector3f(z, 0, -x));
            camera.move(new Vector3f(z, 0, -x));
        }

        if(input.getKeyboard().isKeyPressed(KeyboardKeys.W_KEY)){
            move(new Vector3f(-x, 0, -z));
            camera.move(new Vector3f(-x, 0, -z));
        }

        if(input.getKeyboard().isKeyPressed(KeyboardKeys.S_KEY)){
            move(new Vector3f(x, 0, z));
            camera.move(new Vector3f(x, 0, z));
        }

        if(input.getKeyboard().isKeyPressed(KeyboardKeys.SPACE_KEY)){
            camera.move(0, moveSpeed / 2 * GLFWDisplayManager.getFrameTimeSeconds(), 0);
            move(0, moveSpeed / 2 * GLFWDisplayManager.getFrameTimeSeconds(), 0);

        }

        if(input.getKeyboard().isKeyPressed(KeyboardKeys.LEFT_SHIFT_KEY)){
            camera.move(new Vector3f(0, -moveSpeed / 2 * GLFWDisplayManager.getFrameTimeSeconds(), 0));
            move(new Vector3f(0, -moveSpeed / 2 * GLFWDisplayManager.getFrameTimeSeconds(), 0));

        }

        if(input.getKeyboard().isKeyPressed(KeyboardKeys.LEFT_CONTROL_KEY)){
            moveSpeed = 0.6f;
        }else {
            moveSpeed = 0.2f;
        }

        if(input.getKeyboard().isKeyPressed(KeyboardKeys.SPACE_KEY)){
            jump();
        }
        FIXME
         */

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
        getRotation().set(0, finalRotation.y - 90, 0);
        oldMouseX = newMouseX;
        oldMouseY = newMouseY;
    }

    private void handleControllerInput(){

    }
}
