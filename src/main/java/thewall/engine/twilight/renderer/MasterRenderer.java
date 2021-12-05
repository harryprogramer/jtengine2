package thewall.engine.twilight.renderer;

import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import thewall.engine.twilight.display.GLFWDisplayManager;
import thewall.engine.twilight.display.DisplayUtils;
import thewall.engine.twilight.display.Resolution;
import thewall.engine.twilight.entity.Camera;
import thewall.engine.twilight.entity.Light;
import thewall.engine.twilight.entity.Spatial;
import thewall.engine.twilight.errors.NotImplementedException;
import thewall.engine.twilight.models.Loader;
import thewall.engine.twilight.models.TexturedModel;
import thewall.engine.twilight.renderer.opengl.lwjgl.LwjglGL;
import thewall.engine.twilight.shaders.StaticShader;
import thewall.engine.twilight.shaders.TerrainShader;
import thewall.engine.twilight.skybox.SkyboxRender;
import thewall.engine.twilight.terrain.Terrain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.lwjgl.opengl.GL11.*;

@Deprecated
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
    private final TerrainShader terrainShader = new TerrainShader(null);
    private final SkyboxRender skyboxShader;

    private final List<Terrain> terrains = new ArrayList<>();

    private final List<Class<?>> deprecatedList = new ArrayList<>();

    public MasterRenderer(GLFWDisplayManager glfwDisplayManager, Loader loader){
        this.glfwDisplayManager = glfwDisplayManager;
        GL11.glEnable(GL_CULL_FACE);
        GL11.glCullFace(GL_BACK);
        createProjectionMatrix(-1, -1);
        //entityRenderer = new EntityRenderer(glfwDisplayManager, shader, this);
        terrainRenderer = new TerrainRenderer(terrainShader, getProjectionMatrix());
        skyboxShader = new SkyboxRender(loader, projectionMatrix);
    }

    public Matrix4f getProjectionMatrix(){
        return projectionMatrix;
    }

    private final StaticShader shader = new StaticShader(null);
    @Getter
    private EntityRenderer entityRenderer;

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
        skyboxShader.render(camera);
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
