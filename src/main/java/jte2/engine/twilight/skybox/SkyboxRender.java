package jte2.engine.twilight.skybox;

import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import jte2.engine.twilight.renderer.opengl.GL;
import jte2.engine.twilight.renderer.opengl.GL2;
import jte2.engine.twilight.renderer.opengl.GL3;
import jte2.engine.twilight.renderer.opengl.vao.VAOManager;
import jte2.engine.twilight.shaders.gl.SkyboxShader;
import jte2.engine.twilight.spatials.Camera;
import jte2.engine.twilight.spatials.Spatial;
import jte2.engine.twilight.texture.opengl.GLTextureManager;
import jte2.engine.twilight.utils.Validation;

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

    private final SkyboxShader shader;
    private final GL gl;
    private final GL2 gl2;
    private final GL3 gl3;

    private final int cube;

    public SkyboxRender(@NotNull GLTextureManager loader, Matrix4f projectionMatrix, GL gl, @NotNull VAOManager vaoManager){
        Validation.checkNull(gl);
        this.gl = gl;
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
        this.vertex_count = VERTICES.length / 3;
        this.shader = new SkyboxShader();
        this.shader.setGL(gl);
        this.shader.init();
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void render(Camera camera, Spatial skybox){
        if(skybox != null) {
            shader.start();
            shader.loadViewMatrix(camera);
            gl3.glBindVertexArray(cube);
            gl2.glEnableVertexAttribArray(0);
            gl.glActiveTexture(GL.GL_TEXTURE0);
            gl.glBindTexture(GL.GL_TEXTURE_CUBE_MAP, skybox.getMaterial().getTexture().getID());
            gl.glDrawArrays(GL.GL_TRIANGLES, 0, vertex_count);
            gl2.glDisableVertexAttribArray(0);
            gl2.glBindVertexArray(0);
            shader.stop();
        }
    }

    public void updateMatrix(Matrix4f matrix4f){
        shader.start();
        shader.loadProjectionMatrix(matrix4f);
        shader.stop();
    }
}
