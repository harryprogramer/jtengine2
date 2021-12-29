package thewall.engine.twilight.shaders.gl;

import org.joml.Matrix4f;
import thewall.engine.twilight.renderer.opengl.GL;
import thewall.engine.twilight.renderer.opengl.vao.VAOManager;

public final class GUIShader extends GLShaderProgram {

    private static final String VERTEX_FILE = "opengl/Gui/Gui.vert";
    private static final String FRAGMENT_FILE = "opengl/Gui/Gui.frag";

    private final int vao;
    private int location_transformationMatrix;

    public GUIShader(VAOManager manager) {
        super(VERTEX_FILE, FRAGMENT_FILE);
        this.vao = manager.loadToVAO(new float[]{-1, 1, -1, -1, 1, 1, 1, -1}, 2);
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
