package thewall.engine.twilight.shaders;

import org.joml.Matrix4f;
import org.joml.Random;
import org.joml.Vector2f;
import org.joml.Vector3f;
import thewall.engine.twilight.entity.Camera;
import thewall.engine.twilight.entity.Light;
import thewall.engine.twilight.math.Maths;
import org.jetbrains.annotations.NotNull;
import thewall.engine.twilight.renderer.opengl.GL;
import thewall.engine.twilight.utils.Colour;

import java.util.List;

public final class StaticShader extends ShaderProgram {
    private static final short MAX_LIGHTS = 4;

    private int locationTransformationMatrix;
    private int locationProjectionMatrix;
    private int locationViewMatrix;
    private int[] locationLightColor;
    private int[] locationLightPosition;
    private int locationReflectivity;
    private int locationShaneDamper;
    private int location_useFakeLighting;
    private int locationSkyColor;
    private int locationNumberOfRows;
    private int locationOffset;
    private int[] locationAttenuation;
    private int locationRandom;

    public StaticShader(GL gl){
        super("vertexShader.frag", "fragmentShader.vert", gl);
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
            locationLightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
            locationLightColor[i] = super.getUniformLocation("lightColour[" + i + "]");
            locationAttenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
        }
        locationReflectivity = super.getUniformLocation("reflectivity");
        locationShaneDamper = super.getUniformLocation("shineDamper");
        location_useFakeLighting = super.getUniformLocation("useFakeLighting");
        locationSkyColor = super.getUniformLocation("skyColor");
        locationRandom = super.getUniformLocation("random");
        locationNumberOfRows = super.getUniformLocation("numberOfRows");
        locationOffset = super.getUniformLocation("offset");

        loadFloat(locationRandom, new Random().nextFloat());
    }

    public void loadNumberOfRows(int numberOfRows){
        super.loadFloat(locationNumberOfRows, numberOfRows);
    }

    public void loadOffset(Vector2f vector2f){
        super.loadVector2f(locationOffset, vector2f);
    }

    public void loadSkyColor(Colour colour){
        super.loadVector(locationSkyColor, new Vector3f(colour.getRed(), colour.getGreen(), colour.getBlue()));
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

    public void loadFakeLighting(boolean useFake){
        super.loadBoolean(location_useFakeLighting, useFake);
    }

    public void loadLights(@NotNull List<Light> lights){
        for(int i = 0; i < MAX_LIGHTS; i++) {
            if(i < lights.size()) {
                super.loadVector(locationLightPosition[i], lights.get(i).getPosition());
                super.loadVector(locationLightColor[i], lights.get(i).getColour());
                super.loadVector(locationAttenuation[i], lights.get(i).getAttenuation());
            }else {
                super.loadVector(locationLightColor[i], new Vector3f(0, 0, 0));
                super.loadVector(locationLightPosition[i], new Vector3f(0, 0, 0));
                super.loadVector(locationAttenuation[i], new Vector3f(1, 0, 0));
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
