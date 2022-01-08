package thewall.engine.twilight.renderer.opengl;

import com.google.common.primitives.Floats;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import thewall.engine.twilight.Area;
import thewall.engine.twilight.errors.OpenGLException;
import thewall.engine.twilight.input.Input;
import thewall.engine.twilight.input.mouse.MouseButton;
import thewall.engine.twilight.models.Mesh;
import thewall.engine.twilight.shaders.ShaderHandle;
import thewall.engine.twilight.shaders.gl.*;
import thewall.engine.twilight.spatials.Spatial2D;
import thewall.engine.twilight.system.JTEContext;
import thewall.engine.twilight.utils.MousePicker;
import thewall.engine.twilight.viewport.*;
import thewall.engine.twilight.display.Display;
import thewall.engine.twilight.events.endpoints.EndpointHandler;
import thewall.engine.twilight.spatials.Camera;
import thewall.engine.twilight.spatials.Light;
import thewall.engine.twilight.spatials.Spatial;
import thewall.engine.twilight.errors.TextureDecoderException;
import thewall.engine.twilight.material.Material;
import thewall.engine.twilight.math.Maths;
import thewall.engine.twilight.renderer.Renderer;
import thewall.engine.twilight.renderer.TerrainRenderer;
import thewall.engine.twilight.renderer.opengl.vao.VAOManager;
import thewall.engine.twilight.skybox.SkyboxRender;
import thewall.engine.twilight.terrain.Terrain;
import thewall.engine.twilight.texture.PixelFormat;
import thewall.engine.twilight.texture.TerrainTexture;
import thewall.engine.twilight.texture.TerrainTexturePack;
import thewall.engine.twilight.texture.opengl.GLTextureManager;
import thewall.engine.twilight.material.Colour;
import thewall.engine.twilight.utils.Validation;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static thewall.engine.twilight.renderer.opengl.GL3.*;

/**
    OpenGL renderer for JTEngine
 */
public final class GLRenderer implements Renderer {
    private Colour backgroundColour = Colour.BLACK;
    private final static Logger logger = LogManager.getLogger(GLRenderer.class);
    //private RenderQueue currentQueue = new RenderQueue();
    private Map<Material, List<Spatial>> queue = new HashMap<>();
    private Map<Material, List<Spatial2D>> queue2D = new HashMap<>();
    private volatile ViewPort latestViewport;
    private Matrix4f viewMatrix;

    private boolean isSkybox = false;

    private SkyboxRender skyboxRender;

    private GLShaderProgram shader;
    private GUIShader guiShader;

    private final GL gl;
    private final GL gl2;
    private final GL gl3;
    private final Display display;
    private final VAOManager vaoManager;
    private final GLTextureManager textureManager;

    private final List<Class<?>> deprecatedList = new ArrayList<>();

    private Mesh quadVAO = null;

    private TerrainRenderer terrainRenderer;
    private TerrainShader terrainShader;

    private final List<Terrain> terrains = new ArrayList<>();

    private final EndpointHandler endpointHandler;
    private final Input input;
    private MousePicker mousePicker;

    public GLRenderer(GL gl, VAOManager vao, GLTextureManager glTextureManager, JTEContext context){
        Validation.checkNull(gl);
        Validation.checkNull(vao);
        Validation.checkNull(glTextureManager);
        this.vaoManager = vao;
        this.textureManager = glTextureManager;
        this.display = context.getDisplay();
        this.endpointHandler = context.getEndpointHandler();
        this.input = context.getInput();
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

    public void prepare(){
        gl.glEnable(gl.GL_DEPTH_TEST);
        gl.glClear(gl.GL_COLOR_BUFFER_BIT | gl.GL_DEPTH_BUFFER_BIT);
        gl.glClearColor(backgroundColour.getRedChannel(), backgroundColour.getGreenChannel(), backgroundColour.getBlue(), 1);
    }

    private void createProjectionMatrix(int width, int height, @NotNull ViewPort viewPort) {
        float nearPlane = viewPort.getCamera().getNearPlane();
        float farPlane = viewPort.getCamera().getFarPlane();
        float fov = viewPort.getCamera().getFOV();
        logger.debug(String.format("Creating projection matrix with [FOV: %s | NEARPLANE:  %s | FARPLANE: %s, SCREEN: %dx%d]", fov, nearPlane, farPlane, width, height));

        viewMatrix = Maths.createProjectionMatrix(viewPort, width, height);
    }

    private void unbindTexturedModel(){
        enableCulling();
        gl2.glDisableVertexAttribArray(0);
        gl2.glDisableVertexAttribArray(1);
        gl2.glDisableVertexAttribArray(2);
        gl3.glBindVertexArray(0);
    }

    public void enableCulling(){
        gl.glEnable(GL_CULL_FACE);
        gl.glCullFace(GL_BACK);
    }

    public void disableCulling(){
        gl.glDisable(GL_CULL_FACE);
    }

    private void initShader(@NotNull ShaderHandle program){
        Validation.checkNull(program);
        if(!(program instanceof GLShaderProgram)){
            throw new OpenGLException("Not valid OpenGL valid shader [" + program.getClass().getName() + "]");
        }

        initShader((GLShaderProgram) program);
    }

    private void initShader(@NotNull GLShaderProgram program){
        Validation.checkNull(program);
        if(!program.isInitialized()) {
            program.setGL(gl);
            program.init();
        }
    }

    @Override
    public void init(ViewPort viewPort) {
        setSpatialShader(new StaticShader());
        logger.info("Using shader [" + this.shader.getClass().getName() + "]");

        gl.glEnable(GL_DEPTH_TEST);
        gl.glEnable(GL_CULL_FACE);
        gl.glCullFace(GL_BACK);

        Area windowSize = display.getLocation();
        createProjectionMatrix(windowSize.getWidth(), windowSize.getHeight(), viewPort);
        setViewPort(0, 0, windowSize.getWidth(), windowSize.getHeight());

        display.setSize(new Area(800, 800));
        display.setMinimumSize(new Area(600, 300));

        display.sizeChangedListener((x, y) -> {
            createProjectionMatrix(x, y, this.latestViewport);
            setViewPort(0, 0, x, y);
            if(this.skyboxRender != null){
                skyboxRender.updateMatrix(viewMatrix);
            }
        });

        mousePicker = new MousePicker(viewPort.getCamera(), viewMatrix, input, display);

        this.skyboxRender = new SkyboxRender(textureManager, viewMatrix, gl, vaoManager);
        this.terrainShader = new TerrainShader();
        initShader(terrainShader);
        this.guiShader = new GUIShader(vaoManager);
        initShader(guiShader);
        this.terrainRenderer = new TerrainRenderer(terrainShader, viewMatrix);

        TerrainTexture backgroundTexture = new TerrainTexture(textureManager.loadTexture("grass3", PixelFormat.RGBA));
        TerrainTexture rTexture = new TerrainTexture(textureManager.loadTexture("mud", PixelFormat.RGBA));
        TerrainTexture gTexture = new TerrainTexture(textureManager.loadTexture("grassFlowers", PixelFormat.RGBA));
        TerrainTexture bTexture = new TerrainTexture(textureManager.loadTexture("path", PixelFormat.RGBA));
        TerrainTexture blendMap = new TerrainTexture(textureManager.loadTexture("blendMap", PixelFormat.RGBA));
        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
        Terrain terrain = new Terrain(0, 0, vaoManager, texturePack, blendMap, "heightMap");
        terrains.add(terrain);

        int id = vaoManager.loadToVAO(new float[]{-1, 1, -1, -1, 1, 1, 1 ,-1}, 2);

        if(id == 0 || id == -1){
            throw new OpenGLException("Generated VAO for quad is 0 or -1");
        }

        Mesh mesh = new Mesh();
        mesh.setID(id);
        this.quadVAO = mesh;

        this.shader.start();
        this.shader.loadProjectionMatrix(viewMatrix);
        this.shader.stop();
    }

    private void prepareQueue2D(RenderQueue2D queue2D){
        for (int i = 0; i < queue2D.size(); i++) {
            List<Spatial2D> spatialList = queue2D.get(i).getChildren();
            for (Spatial2D spatial : spatialList) {
                if (spatial.getMesh() == null) {
                    throw new NullPointerException("Mesh is not set");
                }
                if (spatial.getMaterial() == null) {
                    throw new NullPointerException("Material is not set");
                }

                if (spatial.getMesh().getID() == -1) {
                    spatial.getMesh().setID(vaoManager.loadToVAO(Floats.toArray(spatial.getMesh().getVertices()), 2));
                }

                if (spatial.getMaterial().getID() == -1) {
                    Material material = spatial.getMaterial();
                    if (material.getMaterialBuffer() == null || material.getMaterialBuffer().capacity() == 0) {
                        throw new IllegalStateException("Material has null or zero texture buffer");
                    }
                    if (material.getMaterialWidth() == 0 || material.getMaterialHeight() == 0) {
                        throw new TextureDecoderException("Invalid material texture width or height");
                    }
                    int id = textureManager.loadTexture(material.getMaterialBuffer(), material.getMaterialWidth(), material.getMaterialHeight(), material.getMaterialFormat());
                    spatial.getMaterial().setID(id);
                }

            }
        }
        for(int i = 0; i < queue2D.size(); i++){
            Node2D node = queue2D.get(i);
            for(Spatial2D spatial : node.getChildren()) {
                addToQueue2D(spatial);
            }
        }
    }

    private void prepareQueue3D(RenderQueue queue){
        for (int i = 0; i < queue.size(); i++) {
            List<Spatial> spatialList = queue.get(i).getChildren();
            for (Spatial spatial : spatialList) {
                if (spatial.getMesh() == null) {
                    throw new NullPointerException("Mesh is not set");
                }
                if (spatial.getMaterial() == null) {
                    throw new NullPointerException("Material is not set");
                }

                if (spatial.getMesh().getID() == -1) {
                    spatial.getMesh().setID(vaoManager.loadToVAO(spatial.getMesh()));
                }

                if (spatial.getMaterial().getID() == -1) {
                    Material material = spatial.getMaterial();
                    if (material.getMaterialBuffer() == null || material.getMaterialBuffer().capacity() == 0) {
                        throw new IllegalStateException("Material has null or zero texture buffer");
                    }
                    if (material.getMaterialWidth() == 0 || material.getMaterialHeight() == 0) {
                        throw new TextureDecoderException("Invalid material texture width or height");
                    }
                    int id = textureManager.loadTexture(material.getMaterialBuffer(), material.getMaterialWidth(), material.getMaterialHeight(), material.getMaterialFormat());
                    spatial.getMaterial().setID(id);
                }

            }
        }
        for(int i = 0; i < queue.size(); i++){
            Node node = queue.get(i);
            for(Spatial spatial : node.getChildren()) {
                addToQueue3D(spatial);
            }
        }
    }

    @Override
    public void setBackground(Colour colour) {
        Validation.checkNull(colour);
        this.backgroundColour = colour;
    }

    @Override
    public void setViewPort(int x, int y, int width, int height) {
        gl.glViewport(x, y, width, height);
    }

    @Override
    public Matrix4f getProjectionMatrix() {
        return viewMatrix;
    }

    @Override
    public void hideSkybox() {
        this.isSkybox = false;
    }

    @Override
    public void showSkybox() {
        this.isSkybox = true;
    }

    @Override
    public void changeProjectionMatrix(Matrix4f matrix) {
        Validation.checkNull(matrix);
        this.viewMatrix = matrix;
    }

    @Override
    public void prepareRenderQueue(RenderQueue renderQueue, RenderQueue2D renderQueue2D) {
        prepareQueue2D(renderQueue2D);
        prepareQueue3D(renderQueue);
    }

    private void addToQueue2D(@NotNull Spatial2D entity){
        checkForDeprecation(entity.getClass());
        Material entityModel = entity.getMaterial();
        List<Spatial2D> batch = queue2D.get(entityModel);
        if(batch != null){
            batch.add(entity);
        }else {
            List<Spatial2D> newBatch = new ArrayList<>();
            newBatch.add(entity);
            queue2D.put(entityModel, newBatch);
        }
    }

    private void addToQueue3D(@NotNull Spatial entity){
        checkForDeprecation(entity.getClass());
        Material entityModel = entity.getMaterial();
        List<Spatial> batch = queue.get(entityModel);
        if(batch != null){
            batch.add(entity);
        }else {
            List<Spatial> newBatch = new ArrayList<>();
            newBatch.add(entity);
            queue.put(entityModel, newBatch);
        }
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

    public void render2D(@NotNull ViewPort2D viewport){
        guiShader.start();
        gl3.glBindVertexArray(quadVAO.getID());
        gl2.glEnableVertexAttribArray(0);
        gl.glEnable(GL_BLEND);
        gl.glDisable(GL_DEPTH_TEST);

        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        RenderQueue2D queue2D = viewport.getRenderQueue();

        for (Node2D node2D : queue2D) {
            List<Spatial2D> guis = node2D.getChildren();
            for (Spatial2D gui : guis) {
                gl.glActiveTexture(GL_TEXTURE0);
                gl.glBindTexture(GL_TEXTURE_2D, gui.getMaterial().getID());
                Matrix4f matrix = Maths.createTransformationMatrix(gui.getTransformation(), gui.getScale());
                guiShader.loadTransformation(matrix);
                gl.glDrawArrays(GL_TRIANGLE_STRIP, 0, quadVAO.getID());
            }
        }
        gl.glDisable(GL_BLEND);
        gl.glEnable(GL_DEPTH_TEST);
        gl2.glDisableVertexAttribArray(0);
        gl2.glBindVertexArray(0);
        guiShader.stop();
    }

    private void loadUniformToShader(ShaderHandle shader, List<Light> lights, Camera camera){
        shader.loadSkyColor(backgroundColour);
        shader.loadLights(lights);
        shader.loadViewMatrix(camera);
    }

    private void render3D(ViewPort viewPort){
        //logger.info(viewPort.getRenderQueue().size() != 0 ? viewPort.getRenderQueue().size() + " " + viewPort.getRenderQueue().get(0) : "niema jeszczczenaijsdnasd");
        for(Material model : queue.keySet()){
            ShaderHandle shader = model.getShader();
            List<Light> lights = viewPort.getLights();
            Camera camera = viewPort.getCamera();
            if(shader != null){
                this.shader.stop();
                initShader(shader);
                shader.start();
                loadUniformToShader(shader, lights, camera);
            }

            List<Spatial> batch = queue.get(model);
            for(Spatial entity : batch){
                prepareTexturedModel(model, entity);
                prepareInstance(entity);
                gl.glDrawElements(GL_TRIANGLES, entity.getMesh().getCoordinatesSize(), GL_UNSIGNED_INT, 0);
            }

            unbindTexturedModel();
            if(shader != null){
                shader.stop();
                this.shader.start();
                loadUniformToShader(this.shader, lights, camera);
            }
        }
    }

    @Override
    public void render(@NotNull ViewPort viewPort, ViewPort2D viewPort2D) {
        prepare();

        this.latestViewport = viewPort;

        Camera camera = viewPort.getCamera();
        List<Light> lights = viewPort.getLights();

        if(camera == null){
            throw new NullPointerException("Camera is null");
        }

        if(lights == null){
            throw new NullPointerException("Lights is null");
        }

        if(shader == null){
            throw new NullPointerException("Shader is null");
        }

        mousePicker.update();
        if(input.getMouse().mouseReleased(MouseButton.MOUSE_BUTTON_1))
            logger.info(mousePicker.getCurrentRay());

        shader.start();
        shader.loadSkyColor(backgroundColour);
        shader.loadLights(lights);
        shader.loadViewMatrix(camera);
        render3D(viewPort);
        shader.stop();

        Spatial skybox = viewPort.getSkybox();

        if(skybox != null) {
            skyboxRender.render(viewPort.getCamera(), skybox);
        }
        render2D(viewPort2D);

        gl.glFlush();

        queue2D.clear();
        queue.clear();
    }

    private void prepareTexturedModel(@NotNull Material material, Spatial spatial){
        int id = spatial.getMesh().getID();
        int materialID = material.getID();

        if(materialID == 0 || materialID == -1){
            materialID =  textureManager.loadTexture(material.getMaterialBuffer(), material.getMaterialWidth(), material.getMaterialHeight() ,material.getMaterialFormat());
            //throw new OpenGLException("Material is not bind or ID is null_ptr");
        }

        if(id == 0 || id == -1){
            throw new OpenGLException("Spatial VAO ID is null_ptr");
        }
        gl3.glBindVertexArray(id);
        gl2.glEnableVertexAttribArray(0);
        gl2.glEnableVertexAttribArray(1);
        gl2.glEnableVertexAttribArray(2);
        shader.loadNumberOfRows(material.getMultiTextureRows());
        if(material.isTransparency()){
            disableCulling();
        }
        shader.loadFakeLighting(material.isFakeLighting());
        shader.loadShineVariables(material.getShineDamper(), material.getReflectivity());
        gl.glActiveTexture(gl.GL_TEXTURE0);
        gl.glBindTexture(GL_TEXTURE_2D, materialID);
    }

    private void prepareInstance(@NotNull Spatial entity){
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getTransformation(),
                entity.getRotation(), entity.getSize());
        shader.loadTransformationMatrix(transformationMatrix);
        shader.loadOffset(new Vector2f(entity.getMaterial().getTextureXOffset(), entity.getMaterial().getTextureYOffset()));
    }

    @Override
    public void setSpatialShader(ShaderHandle shader) {
        if(!(shader instanceof GLShaderProgram)){
            throw new OpenGLException("Not OpenGL valid shader [" + shader.getClass().getName() + "]");
        }

        if(shader == this.shader){
            return; // if this shader is already set, skip this function
        }
        logger.info("Loading shader [" + shader.getClass().getName() + "]");

        this.shader = (GLShaderProgram) shader;
        this.shader.setGL(gl);
        this.shader.init();
    }

    @Override
    public void setTerrainShader(ShaderHandle shader) {

    }

    @Override
    public void cleanUp() {
        logger.info("Cleaning VAO list");

        this.vaoManager.cleanUp();
        logger.info("Cleaning textures");
        this.textureManager.cleanUp();
    }

    @Override
    public void takeScreenShot(String outputFile) {
        // TODO screenshot
    }

    @Override
    public void takeScreenShot(BufferedImage buffer) {
        // TODO screenshot
    }

    @Override
    public void takeScreenShot(ByteBuffer buffer) {
        // TODO screenshot
    }

    @Override
    public String getName() {
        return "OpenGL Renderer " + gl.glGetString(gl.GL_VERSION);
    }
}
