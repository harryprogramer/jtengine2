package jte2.engine.twilight.renderer.opengl;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import jte2.engine.twilight.material.Colour;
import jte2.engine.twilight.material.Material;
import jte2.engine.twilight.math.Maths;
import jte2.engine.twilight.shaders.gl.MappingShader;
import jte2.engine.twilight.spatials.Camera;
import jte2.engine.twilight.spatials.Light;
import jte2.engine.twilight.spatials.Spatial;
import jte2.engine.twilight.viewport.ViewPort;

import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static jte2.engine.twilight.renderer.opengl.GL.GL_CULL_FACE;

public class GLMappingRenderer {
    private final GL gl;
    private final GL gl2;
    private final GL gl3;

    private MappingShader shader;

    public GLMappingRenderer(Matrix4f projectionMatrix, GL gl) {
        this.gl = gl;
        this.gl2 = gl instanceof GL2 ? (GL2) gl : null;
        this.gl3 = gl instanceof GL3 ? (GL3) gl : null;

        String error = "";

        if(gl == null){
            error = "OpenGL 1.0+ is not supported";
        }else if(gl2 == null){
            error = "OpenGL 2.0+ is not supported";
        }else if(gl3 == null){
            error = "OpenGL 3.0+ is not supported";
        }

        if(gl == null || gl2 == null || gl3 == null){
            throw new RuntimeException(error);
        }

        this.shader = new MappingShader();
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.connectTextureUnits();
        shader.stop();
    }

    public void render(ViewPort viewPort, Vector4f clipPlane, Map<Material, List<Spatial>> queue) {
        shader.start();
        prepare(clipPlane, viewPort.getLights(), viewPort.getCamera());
        for(Material model : queue.keySet()){
            List<Spatial> batch = queue.get(model);
            for(Spatial entity : batch){
                prepareTexturedModel(entity);
                prepareInstance(entity);
                gl.glDrawElements(GL_TRIANGLES, entity.getMesh().getCoordinatesSize(), GL_UNSIGNED_INT, 0);
            }

            unbindTexturedModel();
        }
    }

    public void cleanUp(){
        shader.cleanUp();
    }

    private void prepareTexturedModel(Spatial model) {
        gl3.glBindVertexArray(model.getMesh().getID());
        gl2.glEnableVertexAttribArray(0);
        gl2.glEnableVertexAttribArray(1);
        gl2.glEnableVertexAttribArray(2);
        gl2.glEnableVertexAttribArray(3);
        shader.loadNumberOfRows(model.getMaterial().getMultiTextureRows());
        if (model.getMaterial().isTransparency()) {
            gl.glDisable(GL_CULL_FACE);
        }
        shader.loadShineVariables(model.getMaterial().getShineDamper(), model.getMaterial().getReflectivity());
        gl.glActiveTexture(gl.GL_TEXTURE0);
        gl.glBindTexture(gl.GL_TEXTURE_2D, model.getMaterial().getID());
        gl.glActiveTexture(gl.GL_TEXTURE1);
        //FIXME: gl.glBindTexture(gl.GL_TEXTURE_2D, model.getMaterial().getNormalMap());
    }

    private void unbindTexturedModel() {
        gl.glEnable(GL_CULL_FACE);
        gl2.glDisableVertexAttribArray(0);
        gl2.glDisableVertexAttribArray(1);
        gl2.glDisableVertexAttribArray(2);
        gl2.glDisableVertexAttribArray(3);
        gl2.glBindVertexArray(0);
    }

    private void prepareInstance(Spatial entity) {
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getTransformation(), entity.getRotation(), entity.getSize());
        shader.loadTransformationMatrix(transformationMatrix);
        shader.loadOffset(entity.getMaterial().getTextureXOffset(), entity.getMaterial().getTextureYOffset());
    }

    private void prepare(Vector4f clipPlane, List<Light> lights, Camera camera) {
        shader.loadClipPlane(clipPlane);
        shader.loadSkyColour(Colour.GREEN);
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);

        shader.loadLights(lights, viewMatrix);
        shader.loadViewMatrix(viewMatrix);
    }
}
