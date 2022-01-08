package thewall.engine.twilight.utils;

import io.swagger.models.auth.In;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import thewall.engine.twilight.display.Display;
import thewall.engine.twilight.input.Input;
import thewall.engine.twilight.math.Maths;
import thewall.engine.twilight.math.Vector2f;
import thewall.engine.twilight.math.Vector3f;
import thewall.engine.twilight.spatials.Camera;

public class MousePicker {
    private Vector3f currentRay;
    private Matrix4f projectionMatrix;
    private Matrix4f viewMatrix;
    private Camera camera;
    private Display display;
    private Input input;

    public MousePicker(Camera cam, Matrix4f projection, Input input, Display display){
        this.camera = cam;
        this.projectionMatrix = projection;
        this.viewMatrix = Maths.createViewMatrix(camera);
        this.input = input;
        this.display = display;
    }

    public Vector3f getCurrentRay(){
        return currentRay;
    }

    public void update(){
        viewMatrix = Maths.createViewMatrix(camera);
        currentRay = calculateMouseRay();
    }

    private Vector3f calculateMouseRay(){
        float mouseX = (float) input.getMouse().getCursorPosition().getXPos();
        float mouseY = (float) input.getMouse().getCursorPosition().getYPos();
        Vector2f normalized = getNormalizedDeviceCoors(mouseX, mouseY);
        Vector4f clip = new Vector4f(normalized.x, normalized.y, -1f, 1f);
        Vector4f eye = toEye(clip);
        return toWorld(eye);
    }

    private Vector3f toWorld(Vector4f eye){
        Matrix4f inverted = new Matrix4f().invert(viewMatrix);
        Vector4f rawWorld = transform(inverted, eye, null);
        Vector3f mouseRay = new Vector3f(rawWorld.x, rawWorld.y, rawWorld.z);
        mouseRay.normalize();
        return mouseRay;
    }

    private Vector4f toEye(Vector4f clip){
        Matrix4f inverted = new Matrix4f().invert(projectionMatrix);
        Vector4f eye = transform(inverted, clip, null);
        return new Vector4f(eye.x, eye.y, -1f, 0f);
    }

    public static Vector4f transform(Matrix4f left, Vector4f right, Vector4f dest) {
        if (dest == null)
            dest = new Vector4f();

        float x = left.m00() * right.x + left.m10() * right.y + left.m20() * right.z + left.m30() * right.w;
        float y = left.m01() * right.x + left.m11() * right.y + left.m21() * right.z + left.m31() * right.w;
        float z = left.m02() * right.x + left.m12() * right.y + left.m22() * right.z + left.m32() * right.w;
        float w = left.m03() * right.x + left.m13() * right.y + left.m23() * right.z + left.m33() * right.w;

        dest.x = x;
        dest.y = y;
        dest.z = z;
        dest.w = w;

        return dest;
    }

    private Vector2f getNormalizedDeviceCoors(float mouseX, float mouseY){
        float x = (2f*mouseX) / display.getLocation().getHeight() - 1;
        float y = (2f*mouseY) / display.getLocation().getWidth() - 1f;
        return new Vector2f(x, y);
    }
}
