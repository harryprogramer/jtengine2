package thewall.engine.twilight.shaders.gl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import thewall.engine.twilight.errors.OpenGLException;
import thewall.engine.twilight.material.Colour;
import thewall.engine.twilight.renderer.opengl.GL;
import thewall.engine.twilight.renderer.opengl.GL2;
import thewall.engine.twilight.renderer.opengl.GL3;
import thewall.engine.twilight.shaders.ShaderHandle;
import thewall.engine.twilight.spatials.Camera;
import thewall.engine.twilight.spatials.Light;
import thewall.engine.twilight.utils.ResourceUtils;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.BufferUtils;
import thewall.engine.twilight.utils.Validation;

import java.nio.FloatBuffer;
import java.util.List;

public abstract class GLShaderProgram implements ShaderHandle {
    private static final Logger logger = LogManager.getLogger(GLShaderProgram.class);

    private int programID;
    private int vertexShaderID;
    private int fragmentShaderID;

    private static final FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    protected GL gl;
    protected GL2 gl2;
    protected GL3 gl3;

    private boolean isInit = false;

    private final String vertexFile, fragmentFile;

    public GLShaderProgram(String vertexFile, String fragmentFile){
        Validation.checkNull(vertexFile, fragmentFile);
        this.vertexFile = vertexFile;
        this.fragmentFile = fragmentFile;
    }

    public boolean isInitialized(){
        return isInit;
    }

    @Override
    public void init() {
        if(gl == null){
            throw new OpenGLException("GL is not set");
        }

        this.vertexShaderID = loadShader(vertexFile, GL2.GL_VERTEX_SHADER, gl2);
        this.fragmentShaderID = loadShader(fragmentFile, GL2.GL_FRAGMENT_SHADER, gl2);
        this.programID = gl2.glCreateProgram();
        gl2.glAttachShader(programID, vertexShaderID);
        gl2.glAttachShader(programID, fragmentShaderID);
        bindAttributes();
        gl2.glLinkProgram(programID);
        gl2.glValidateProgram(programID);
        String errorShader = gl2.glGetProgramInfoLog(programID);
        if(errorShader.length() != 0) {
            logger.error(errorShader);
        }
        getAllUniformLocations();

        isInit = true;
    }

    public void start(){
        gl2.glUseProgram(programID);
    }

    public void setGL(GL gl){
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
    }

    public void stop(){
        gl2.glUseProgram(0);
    }

    public void cleanUp(){
        stop();
        gl2.glDetachShader(programID, vertexShaderID);
        gl2.glDetachShader(programID, fragmentShaderID);
        gl2.glDeleteShader(vertexShaderID);
        gl2.glDeleteShader(fragmentShaderID);
        gl2.glDeleteProgram(programID);
    }

    protected int getUniformLocation(String uniformName){
        int location = gl2.glGetUniformLocation(programID, uniformName);
        if(location == -1){
            logger.error("Uniform from [" + this.getClass().getPackageName() + "." + this.getClass().getSimpleName() + "] [" + uniformName + "] not found");
        }

        return location;
    }

    protected void loadFloat(int location, float value){
        gl2.glUniform1f(location, value);
    }

    protected void loadVector(int location, @NotNull Vector3f vector3f){
        gl2.glUniform3f(location, vector3f.x, vector3f.y, vector3f.z);
    }

    protected void loadInt(int location , @NotNull int value){
        gl2.glUniform1i(location, value);
    }

    protected void loadBoolean(int location, boolean value){
        gl2.glUniform1f(location, value ? 1 : 0);
    }

    protected void loadMatrix(int location, @NotNull Matrix4f matrix4f){
        matrix4f.get(matrixBuffer);
        gl2.glUniformMatrix4fv(location, false, matrixBuffer);
    }

    protected void loadVector2f(int location, @NotNull Vector2f vector){
        gl2.glUniform2f(location, vector.x, vector.y);
    }


    protected abstract void getAllUniformLocations();

    @SneakyThrows
    private static int loadShader(String file, int type, GL2 gl){
        int shaderID = gl.glCreateShader(type);
        gl.glShaderSource(shaderID, ResourceUtils.readFromInputStream(GLShaderProgram.class.getResourceAsStream("/shaders/" + file)));
        gl.glCompileShader(shaderID);
        if(gl.glGetShaderi(shaderID, gl.GL_COMPILE_STATUS) == GL.GL_FALSE){
            logger.error("OpenGL GLSL shader compile error from: [" + file + "]");
            logger.error("OpenGL shader error details:\n" + gl.glGetShaderInfoLog(shaderID));
        }
        return shaderID;
    }

    protected abstract void bindAttributes();

    protected void bindAttribute(int attribute, String variableName){
        gl2.glBindAttribLocation(programID, attribute, variableName);
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
    public void loadProjectionMatrix(Matrix4f matrix4f) {}

    @Override
    public void loadViewMatrix(Camera camera) {}

    @Override
    public void loadShineVariables(float dumper, float reflectivity) {}

    @Override
    public void loadSkyColor(Colour colour) {}

    @Override
    public void loadTransformationMatrix(Matrix4f matrix4f) {}
}
