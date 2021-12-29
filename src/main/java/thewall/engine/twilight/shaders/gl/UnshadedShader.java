package thewall.engine.twilight.shaders.gl;

import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import thewall.engine.twilight.material.Colour;
import thewall.engine.twilight.math.Maths;
import thewall.engine.twilight.renderer.opengl.GL;
import thewall.engine.twilight.spatials.Camera;
import thewall.engine.twilight.spatials.Light;

import java.util.List;

public class UnshadedShader extends GLShaderProgram {
    private int transformationMatrix;
    private int projectionMatrix;
    private int viewMatrix;

    public UnshadedShader() {
        super("opengl/Misc/Unshaded.vert", "opengl/Misc/Unshaded.frag");
    }

    @Override
    protected void getAllUniformLocations() {
        this.transformationMatrix = super.getUniformLocation("transformationMatrix");
        this.viewMatrix =           super.getUniformLocation("viewMatrix");
        this.projectionMatrix =     super.getUniformLocation("projectionMatrix");
    }

    public void loadViewMatrix(Camera camera){
        super.loadMatrix(viewMatrix, Maths.createViewMatrix(camera));
    }

    @Override
    public void loadFakeLighting(boolean useFake) {}

    @Override
    public void loadLights(@NotNull List<Light> lights) {}

    @Override
    public void loadNumberOfRows(int numberOfRows) {}

    @Override
    public void loadOffset(Vector2f vector2f) {}

    @Override
    public void loadSkyColor(Colour colour) {}

    public void loadTransformationMatrix(Matrix4f matrix4f){
        super.loadMatrix(transformationMatrix, matrix4f);
    }

    @Override
    public void loadShineVariables(float dumper, float reflectivity) {}

    public void loadProjectionMatrix(Matrix4f matrix4f){
        super.loadMatrix(projectionMatrix, matrix4f);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoord");
    }
}
