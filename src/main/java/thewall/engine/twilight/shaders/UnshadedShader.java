package thewall.engine.twilight.shaders;

import org.joml.Matrix4f;
import thewall.engine.twilight.math.Maths;
import thewall.engine.twilight.renderer.opengl.GL;
import thewall.engine.twilight.spatials.Camera;

public class UnshadedShader extends GLShaderProgram {
    private int transformationMatrix;
    private int projectionMatrix;
    private int viewMatrix;

    public UnshadedShader(GL gl) {
        super("opengl/Misc/Unshaded.vert", "opengl/Misc/Unshaded.frag", gl);
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

    public void loadTransformationMatrix(Matrix4f matrix4f){
        super.loadMatrix(transformationMatrix, matrix4f);
    }

    public void loadProjectionMatrix(Matrix4f matrix4f){
        super.loadMatrix(projectionMatrix, matrix4f);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoord");
    }
}
