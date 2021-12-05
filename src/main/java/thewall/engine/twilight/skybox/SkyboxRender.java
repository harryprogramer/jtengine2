package thewall.engine.twilight.skybox;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import thewall.engine.twilight.entity.Camera;
import thewall.engine.twilight.models.Loader;
import thewall.engine.twilight.renderer.opengl.GL;
import thewall.engine.twilight.renderer.opengl.GL2;
import thewall.engine.twilight.renderer.opengl.GL3;
import thewall.engine.twilight.renderer.opengl.GL4;
import thewall.engine.twilight.renderer.opengl.vao.VAOManager;
import thewall.engine.twilight.shaders.SkyboxShader;
import thewall.engine.twilight.texture.opengl.GLTextureManager;
import thewall.engine.twilight.utils.Validation;

@Deprecated
public final class SkyboxRender {
    private static final float SIZE = 9000f;

    private static final float[] VERTICES = {
            -SIZE,  SIZE, -SIZE,
            -SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE,  SIZE, -SIZE,
            -SIZE,  SIZE, -SIZE,

            -SIZE, -SIZE,  SIZE,
            -SIZE, -SIZE, -SIZE,
            -SIZE,  SIZE, -SIZE,
            -SIZE,  SIZE, -SIZE,
            -SIZE,  SIZE,  SIZE,
            -SIZE, -SIZE,  SIZE,

            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,

            -SIZE, -SIZE,  SIZE,
            -SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE, -SIZE,  SIZE,
            -SIZE, -SIZE,  SIZE,

            -SIZE,  SIZE, -SIZE,
            SIZE,  SIZE, -SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            -SIZE,  SIZE,  SIZE,
            -SIZE,  SIZE, -SIZE,

            -SIZE, -SIZE, -SIZE,
            -SIZE, -SIZE,  SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            -SIZE, -SIZE,  SIZE,
            SIZE, -SIZE,  SIZE
    };

    private int vertex_count = 0;

    private static final String[] TEXTURE_FILES = {"skybox/right.png", "skybox/left.png", "skybox/top.png",
            "skybox/bottom.png", "skybox/back.png", "skybox/front.png"};

    private final int texture;
    private final SkyboxShader shader;
    private final GL gl;
    private final GL2 gl2;
    private final GL3 gl3;

    private final int cube;

    public SkyboxRender(@NotNull GLTextureManager loader, Matrix4f projectionMatrix, GL gl, @NotNull VAOManager vaoManager){
        Validation.checkNull(gl);
        this.gl = gl != null ? (GL) gl : null;
        this.gl2 = gl instanceof GL2 ? (GL2) gl : null;
        this.gl3 = gl instanceof GL3 ? (GL3) gl : null;

        String error = "";

        if(gl == null){
            error = "OpenGL 1.0+ must be supported";
        }else if(gl2 == null){
            error = "OpenGL 2.0+ must be supported";
        }else if(gl3 == null){
            error = "OpenGL 3.0+ must be supported";
        }

        if(gl == null || gl2 == null || gl3 == null){
            throw new RuntimeException(error);
        }

        this.cube = vaoManager.loadToVAO(VERTICES, 3);
        this.texture = loader.load3DTexture(TEXTURE_FILES);
        this.vertex_count = VERTICES.length / 3;
        this.shader = new SkyboxShader(gl);
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    @Contract("_, _ -> fail")
    public SkyboxRender(@NotNull Loader loader, Matrix4f projectionMatrix){
        throw new UnsupportedOperationException(loader.getClass().getName() + " is no longer supported");
    }

    public void render(Camera camera){
        shader.start();
        shader.loadViewMatrix(camera);
        gl3.glBindVertexArray(cube);
        gl2.glEnableVertexAttribArray(0);
        gl.glActiveTexture(GL.GL_TEXTURE0);
        gl.glBindTexture(GL.GL_TEXTURE_CUBE_MAP, texture);
        gl.glDrawArrays(GL.GL_TRIANGLES, 0, vertex_count);
        gl2 .glDisableVertexAttribArray(0);
        gl2.glBindVertexArray(0);
        shader.stop();
    }
}
