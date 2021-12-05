package thewall.engine.twilight.shaders;

import org.joml.Matrix4f;
import thewall.engine.twilight.renderer.opengl.GL;

public final class GuiShader extends ShaderProgram{

    private static final String VERTEX_FILE = "gui/guiVertexShader.vert";
    private static final String FRAGMENT_FILE = "gui/guiFragmentShader.frag";

    private int location_transformationMatrix;

    public GuiShader(GL gl) {
        super(VERTEX_FILE, FRAGMENT_FILE, gl);
    }

    public void loadTransformation(Matrix4f matrix){
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
}
