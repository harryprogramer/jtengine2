package thewall.engine.twilight.shaders.gl;

import org.joml.Matrix4f;
import thewall.engine.twilight.spatials.Camera;
import thewall.engine.twilight.math.Maths;
import thewall.engine.twilight.renderer.opengl.GL;

public final class SkyboxShader extends GLShaderProgram {

	private static final String VERTEX_FILE = "opengl/Skybox/Skybox.vert";
	private static final String FRAGMENT_FILE = "opengl/Skybox/Skybox.frag";
	
	private int location_projectionMatrix;
	private int location_viewMatrix;
	
	public SkyboxShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix){
		super.loadMatrix(location_projectionMatrix, matrix);
	}

	public void loadViewMatrix(Camera camera){
		Matrix4f matrix = Maths.createViewMatrix(camera);
		matrix.m30(0);
		matrix.m31(0);
		matrix.m32(0);
		super.loadMatrix(location_viewMatrix, matrix);
	}
	
	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}
