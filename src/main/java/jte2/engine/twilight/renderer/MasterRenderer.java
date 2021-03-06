package jte2.engine.twilight.renderer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import jte2.engine.twilight.display.GLFWDisplayManager;
import jte2.engine.twilight.display.DisplayUtils;
import jte2.engine.twilight.display.Resolution;
import jte2.engine.twilight.spatials.Camera;
import jte2.engine.twilight.spatials.Light;
import jte2.engine.twilight.spatials.Spatial;
import jte2.engine.twilight.errors.NotImplementedException;
import jte2.engine.twilight.models.Loader;
import jte2.engine.twilight.models.TexturedModel;
import jte2.engine.twilight.shaders.gl.StaticShader;
import jte2.engine.twilight.shaders.gl.TerrainShader;
import jte2.engine.twilight.skybox.SkyboxRender;
import jte2.engine.twilight.terrain.Terrain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.lwjgl.opengl.GL11.*;

@Deprecated(forRemoval = true)
public class MasterRenderer {
    private final static Logger logger = LogManager.getLogger(MasterRenderer.class);

    private final static float FOV = 80;
    private final static float NEAR_PLANE = 0.1f;
    private final static float FAR_PLANE = 17000;

    private static final float RED = 0.6f;
    private static final float GREEN = 0.78f;
    private static final float BLUE = 0.76f;

    private volatile Matrix4f projectionMatrix;
    private final GLFWDisplayManager glfwDisplayManager;
    private final TerrainRenderer terrainRenderer;
    private final TerrainShader terrainShader = new TerrainShader();
    private final SkyboxRender skyboxShader = null;

    private final List<Terrain> terrains = new ArrayList<>();

    private final List<Class<?>> deprecatedList = new ArrayList<>();

    public MasterRenderer(GLFWDisplayManager glfwDisplayManager, Loader loader){
        this.glfwDisplayManager = glfwDisplayManager;
        GL11.glEnable(GL_CULL_FACE);
        GL11.glCullFace(GL_BACK);
        createProjectionMatrix(-1, -1);
        //entityRenderer = new EntityRenderer(glfwDisplayManager, shader, this);
        terrainRenderer = new TerrainRenderer(terrainShader, getProjectionMatrix());
        //skyboxShader = new SkyboxRender(loader, projectionMatrix); function removed 06.01.2022
    }

    public Matrix4f getProjectionMatrix(){
        return projectionMatrix;
    }

    private final StaticShader shader = new StaticShader();

    private final Map<TexturedModel, List<Spatial>> entities = new HashMap<>();

    public void processEntity(@NotNull Spatial entity){
        /*
        checkForDeprecation(entity.getClass());
        TexturedModel entityModel = entity.getModel();
        if(entityModel == null){

        }
        List<Entity> batch = entities.get(entityModel);
        if(batch != null){
            batch.add(entity);
        }else {
            List<Entity> newBatch = new ArrayList<>();
            newBatch.add(entity);
            entities.put(entityModel, newBatch);
        }

         */
        throw new NotImplementedException();
    }

    private void checkForDeprecation(@NotNull Class<?> entity){
        AtomicBoolean isClassWarned = new AtomicBoolean(false);

        deprecatedList.forEach((aClass -> {
            if(aClass.equals(entity)){
                isClassWarned.set(true);
            }
        }));

        if(isClassWarned.get()){
            return;
        }

        if(entity.isAnnotationPresent(Deprecated.class)){
            logger.warn("Entity [" + entity.getPackageName() + "." + entity.getSimpleName() + "] is deprecated, try to use another or implement your own.");
        }

        deprecatedList.add(entity);
    }


    public void render(List<Light> lights, Camera camera){
        prepare();
        shader.start();
       // shader.loadSkyColor(RED, GREEN, BLUE);
        shader.loadLights(lights);
        shader.loadViewMatrix(camera);
        //entityRenderer.render(entities);
        shader.stop();
        terrainShader.start();
        terrainShader.loadSkyColor(RED, GREEN, BLUE);
        terrainShader.loadLights(lights);
        terrainShader.loadViewMatrix(camera);
        terrainRenderer.render(terrains);
        terrainShader.stop();
        skyboxShader.render(camera, null);
        terrains.clear();
        entities.clear();
    }

    public void cleanUp(){
        shader.cleanUp();
        terrainShader.cleanUp();

    }

    private void createProjectionMatrix(int width, int height){
        Resolution resolution;

        if(width != -1 && height != -1){
            resolution = new Resolution(width, height);
        }else {
            resolution = DisplayUtils.getWindowSize(glfwDisplayManager.getWindow());

        }
        float aspectRatio = (float) resolution.getWidth() / (float) resolution.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f();

        projectionMatrix.m00(x_scale);
        projectionMatrix.m11(y_scale);
        projectionMatrix.m22(-((FAR_PLANE + NEAR_PLANE) / frustum_length));
        projectionMatrix.m23(-1);
        projectionMatrix.m32(-((2 * NEAR_PLANE * FAR_PLANE) / frustum_length));
        projectionMatrix.m33(0);
    }

    public void prepare(){
        glEnable(GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(RED, GREEN, BLUE, 1);
    }

    public void processTerrain(Terrain terrain){
        terrains.add(terrain);
    }

    public void resizeWindow(int argWidth, int argHeight) {
        createProjectionMatrix(argWidth, argHeight);
        //entityRenderer.rebuildProjectionMatrix();
        terrainRenderer.rebuildMatrix();
        glViewport(0, 0, argWidth,argHeight);

        //adjustProjectionMatrix(width, height); // recalculating projection matrix (only if you are using one)
    }

    public static void enableCulling(){
        GL11.glEnable(GL_CULL_FACE);
        GL11.glCullFace(GL_BACK);
    }

    public static void disableCulling(){
        GL11.glDisable(GL_CULL_FACE);
    }

}
