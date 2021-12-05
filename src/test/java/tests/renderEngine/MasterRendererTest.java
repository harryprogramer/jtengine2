package tests.renderEngine;

import org.joml.Matrix4f;
import org.junit.jupiter.api.Test;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

class MasterRendererTest {
    private final static float FOV = 80;
    private final static float NEAR_PLANE = 0.1f;
    private final static float FAR_PLANE = 1200;


    @Test
    void test(){
        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(16);

        float aspectRatio = (float) 1280 / (float) 720;
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        Matrix4f projectionMatrix = new Matrix4f();
        projectionMatrix.m00(x_scale);
        projectionMatrix.m11(y_scale);
        projectionMatrix.m22(-((FAR_PLANE + NEAR_PLANE) / frustum_length));
        projectionMatrix.m23(-1);
        projectionMatrix.m32(-((2 * NEAR_PLANE * FAR_PLANE) / frustum_length));
        projectionMatrix.m33(0);

        projectionMatrix.get(floatBuffer);
        //floatBuffer.flip();
        System.out.println(floatBuffer.capacity());
        for(int i = 0; i < floatBuffer.capacity(); i++){
            System.out.println(floatBuffer.get(i) + ", ");
        }
    }
}