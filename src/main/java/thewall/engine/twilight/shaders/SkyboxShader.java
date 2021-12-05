package thewall.engine.twilight.shaders;

import org.joml.Matrix4f;
import thewall.engine.twilight.entity.Camera;
import thewall.engine.twilight.math.Maths;
import thewall.engine.twilight.renderer.opengl.GL;

public final class SkyboxShader extends ShaderProgram{

	private static final String VERTEX_FILE = "skybox/skyboxVertexShader.vert";
	private static final String FRAGMENT_FILE = "skybox/skyboxFragmentShader.frag";
	
	private int location_projectionMatrix;
	private int location_viewMatrix;
	
	public SkyboxShader(GL gl) {
		super(VERTEX_FILE, FRAGMENT_FILE, gl);
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
