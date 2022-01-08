package thewall.engine.twilight.shaders.gl;

import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import thewall.engine.twilight.spatials.Camera;
import thewall.engine.twilight.spatials.Light;
import thewall.engine.twilight.math.Maths;

import java.util.List;

public final class TerrainShader extends GLShaderProgram {
    private static final int MAX_LIGHTS = 4;

    private int locationTransformationMatrix;
    private int locationProjectionMatrix;
    private int locationViewMatrix;
    private int[] locationLightColor;
    private int[] locationLightPosition;
    private int[] locationAttenuation;
    private int locationReflectivity;
    private int locationShaneDamper;
    private int locationSkyColor;
    private int locationBackgroundTexture;
    private int locationRTexture;
    private int locationGTexture;
    private int locationBTexture;
    private int locationBlendMap;

    public TerrainShader(){
        super("opengl/Terrain/TerrainLighting.vert", "opengl/Terrain/TerrainLighting.frag");
    }

    @Override
    protected void getAllUniformLocations() {
        locationTransformationMatrix = super.getUniformLocation("transformationMatrix");
        locationProjectionMatrix = super.getUniformLocation("projectionMatrix");
        locationViewMatrix = super.getUniformLocation("viewMatrix");
        locationLightColor = new int[MAX_LIGHTS];
        locationLightPosition = new int[MAX_LIGHTS];
        locationAttenuation = new int[MAX_LIGHTS];
        for(int i = 0; i < MAX_LIGHTS; i++){
            locationLightColor[i] = super.getUniformLocation("lightColour[" + i + "]");
            locationLightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
            locationAttenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
        }
        locationReflectivity = super.getUniformLocation("reflectivity");
        locationShaneDamper = super.getUniformLocation("shineDamper");
        locationSkyColor = super.getUniformLocation("skyColor");
        locationBackgroundTexture = super.getUniformLocation("backgroundTexture");
        locationRTexture = super.getUniformLocation("rTexture");
        locationGTexture = super.getUniformLocation("gTexture");
        locationBTexture = super.getUniformLocation("bTexture");
        locationBlendMap = super.getUniformLocation("blendMap");
    }

    public void connectTextureUnits(){
        super.loadInt(locationBackgroundTexture, 0);
        super.loadInt(locationRTexture, 1);
        super.loadInt(locationGTexture, 2);
        super.loadInt(locationBTexture, 3);
        super.loadInt(locationBlendMap, 4);
    }

    public void loadSkyColor(float r, float g, float b){
        super.loadVector3f(locationSkyColor, new Vector3f(r, g, b));
    }

    public void loadTransformationMatrix(Matrix4f matrix4f){
        super.loadMatrix(locationTransformationMatrix, matrix4f);
    }

    public void loadShineVariables(float dumper, float reflectivity){
        super.loadFloat(locationReflectivity, reflectivity);
        super.loadFloat(locationShaneDamper, dumper);
    }

    public void loadProjectionMatrix(Matrix4f matrix4f){
        super.loadMatrix(locationProjectionMatrix, matrix4f);
    }

    public void loadViewMatrix(Camera camera){
        super.loadMatrix(locationViewMatrix, Maths.createViewMatrix(camera));
    }

    public void loadLights(@NotNull List<Light> lights){
        for(int i = 0; i < MAX_LIGHTS; i++) {
            if(i < lights.size()) {
                super.loadVector3f(locationLightPosition[i], lights.get(i).getPosition());
                super.loadVector3f(locationLightColor[i], lights.get(i).getColour().getVectorColor());
                super.loadVector3f(locationAttenuation[i], lights.get(i).getAttenuation());
            }else {
                super.loadVector3f(locationLightColor[i], new Vector3f(0, 0, 0));
                super.loadVector3f(locationLightPosition[i], new Vector3f(0, 0, 0));
                super.loadVector3f(locationAttenuation[i], new Vector3f(1, 0, 0));
            }
        }
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoord");
        super.bindAttribute(2, "normal");
    }
}
