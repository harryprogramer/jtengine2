package thewall.engine.twilight.texture.opengl;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import thewall.engine.twilight.renderer.opengl.lwjgl.LwjglGL;
import thewall.engine.twilight.texture.PixelFormat;

import static org.junit.jupiter.api.Assertions.*;

class JTEGLTextureManagerTest {
    JTEGLTextureManager textureManager = new JTEGLTextureManager(new LwjglGL());

    @SneakyThrows
    @Test
    void test(){
        GLFW.glfwInit();
        long window = GLFW.glfwCreateWindow(1280, 720, "gowno", 0, 0);
        GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities();
        for(int i = 0; i < 100; i++) {
            long start = System.currentTimeMillis();
            int textureID = textureManager.loadTexture("box", PixelFormat.RGBA);
            long end = System.currentTimeMillis();
            System.out.println("time: " + (end - start) / 1000.0);
            System.out.println("id: " + textureID);
        }
        textureManager.cleanUp();
    }
}