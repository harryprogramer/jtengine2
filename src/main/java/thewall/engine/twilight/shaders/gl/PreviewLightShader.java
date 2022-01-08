package thewall.engine.twilight.shaders.gl;

import org.joml.Matrix4f;
import org.joml.Random;
import org.joml.Vector2f;
import org.joml.Vector3f;
import thewall.engine.twilight.material.Colour;
import thewall.engine.twilight.math.Maths;
import thewall.engine.twilight.spatials.Camera;

public class PreviewLightShader extends GLShaderProgram {

    private int locationTransformationMatrix;
    private int locationProjectionMatrix;
    private int locationViewMatrix;
    private int locationReflectivity;
    private int locationShaneDamper;
    private int location_useFakeLighting;
    private int locationSkyColor;
    private int locationNumberOfRows;
    private int locationOffset;
    private int locationAttenuation;
    private int locationRandom;

    public PreviewLightShader() {
        super("opengl/Light/LightingPreview.frag", "opengl/Light/LightingPreview.vert");
    }

    @Override
    protected void getAllUniformLocations() {
        locationTransformationMatrix = super.getUniformLocation("transformationMatrix");
        locationProjectionMatrix = super.getUniformLocation("projectionMatrix");
        locationViewMatrix = super.getUniformLocation("viewMatrix");
        locationAttenuation = super.getUniformLocation("attenuation");
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
        super.loadVector3f(locationSkyColor, new Vector3f(colour.getRed(), colour.getGreen(), colour.getBlue()));
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

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoord");
        super.bindAttribute(2, "normal");
    }
}
