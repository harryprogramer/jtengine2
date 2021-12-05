package thewall.engine.twilight.math;

import org.jetbrains.annotations.Contract;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3fc;
import oshi.driver.windows.wmi.Win32Bios;
import thewall.engine.twilight.entity.Camera;

public class Maths {
    static int index = 0;
    private static double[] cos = new double[361];
    private static double[] sin = new double[361];
    private static boolean isCosSineTableIndexed = false;


    public static void generateIndexedCosSineTable(){
        for (int i = 0; i <= 360; i++) {
            cos[i] = Math.cos(Math.toRadians(i));
            sin[i] = Math.sin(Math.toRadians(i));
        }
    }

    public static double getSine(int angle) {
        int angleCircle = angle % 360;
        return sin[angleCircle];
    }

    public static double getCos(int angle) {
        int angleCircle = angle % 360;
        return cos[angleCircle];
    }

    @Contract(pure = true)
    public static float barryCentric(@NotNull Vector3f p1, @NotNull Vector3f p2, @NotNull Vector3f p3, @NotNull Vector2f pos) {
        float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
        float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
        float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
        float l3 = 1.0f - l1 - l2;
        return l1 * p1.y + l2 * p2.y + l3 * p3.y;
    }


    public static @NotNull Matrix4f createTransformationMatrix(Vector3f translation, @NotNull Vector3f rotation, float scale) {
        Matrix4f matrix = new Matrix4f();
        matrix.identity();
        matrix.translate(translation);
        matrix.rotate((float) Math.toRadians(rotation.x), new Vector3f(1,0,0));
        matrix.rotate((float) Math.toRadians(rotation.y), new Vector3f(0,1,0));
        matrix.rotate((float) Math.toRadians(rotation.z), new Vector3f(0,0,1));
        matrix.scale(new Vector3f(scale,scale,scale));
        return matrix;
    }

    public static @NotNull Matrix4f createTransformationMatrix(@NotNull Vector2f translation, @NotNull Vector2f scale) {
        Matrix4f matrix = new Matrix4f();
        matrix.identity();
        matrix.translate(new Vector3f(translation.x, translation.y, 0));
        matrix.scale(new Vector3f(scale.x, scale.y, 1f));
        return matrix;
    }

    public static @NotNull Matrix4f createViewMatrix(@NotNull Camera camera) {
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.identity();
        viewMatrix.rotate((float) Math.toRadians(-camera.getRotation().x), new Vector3f(1, 0, 0));
        viewMatrix.rotate((float) Math.toRadians(-camera.getRotation().y), new Vector3f(0, 1, 0));
        viewMatrix.rotate((float) Math.toRadians(-camera.getRotation().z), new Vector3f(0, 0, 1));
        Vector3f cameraPos = camera.getTransformation();
        Vector3f negativeCameraPos = new Vector3f(-cameraPos.x,-cameraPos.y,-cameraPos.z);
        viewMatrix.translate(negativeCameraPos);
        return viewMatrix;
    }


}
