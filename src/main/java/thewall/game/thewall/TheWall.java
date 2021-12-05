package thewall.game.thewall;

import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;
import org.joml.Vector3f;
import thewall.engine.twilight.TwilightApp;
import thewall.engine.twilight.entity.*;
import thewall.engine.twilight.gui.GuiTexture;
import thewall.engine.twilight.gui.imgui.ImGuiDesigner;
import thewall.engine.twilight.gui.imgui.OnImmediateGUI;
import thewall.engine.twilight.models.TexturedModel;
import thewall.engine.twilight.models.obj.thinmatrix.ModelData;
import thewall.engine.twilight.models.obj.thinmatrix.OBJFileLoader;
import thewall.engine.twilight.terrain.Terrain;
import thewall.engine.twilight.texture.ModelTexture;
import thewall.engine.twilight.texture.TerrainTexture;
import thewall.engine.twilight.texture.TerrainTexturePack;
import thewall.game.thewall.events.GamepadEvent;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

@Deprecated(forRemoval = true, since = "9.0+")
public class TheWall extends TwilightApp {
    static int frameCount = 0;

    static double previousTime = glfwGetTime();

    static int fps = 0;

    private static final long  MEGABYTE = 1024L * 1024L;

    private final static Logger logger = LogManager.getLogger(TheWall.class);

    private final ModelData treeModelData = OBJFileLoader.loadOBJ("tree");
    private final ModelData grassModelData = OBJFileLoader.loadOBJ("fern");
    private final ModelData lowPolyTree = OBJFileLoader.loadOBJ("lowPolyTree");

    private Model treeModel;

    private TexturedModel treemodel;
    private TexturedModel grassModel;

    private TexturedModel lowPolyTreeModel;

    private ModelTexture texture;

    private ModelData modelData = OBJFileLoader.loadOBJ("bunny");
    private Model bunnyModel;
    private Player player;

    private final List<Light> lights = new ArrayList<>();

    private TerrainTexture backgroundTexture;
    private TerrainTexture rTexture ;
    private TerrainTexture gTexture ;
    private TerrainTexture bTexture ;

    private TerrainTexturePack texturePack;
    private TerrainTexture blendMap;

    private Terrain terrain;
    private Terrain terrain2;

    private List<Spatial> worldEntities = new ArrayList<>();

    private final List<GuiTexture> guis = new ArrayList<>();

    public TheWall() {
        setName("The Wall");
        throw new UnsupportedOperationException("No longer supported");
    }

    @Override
    public void onEnable() {
        /*
        bunnyModel = getLoader().loadToVAO(modelData.getVertices(), modelData.getIndices(), modelData.getTextureCoords(), modelData.getNormals());
        grassModel = new TexturedModel(getLoader().loadToVAO(grassModelData.getVertices(), grassModelData.getIndices(),
                grassModelData.getTextureCoords(), grassModelData.getNormals()), new ModelTexture(getLoader().loadTexture("fern", GL_RGBA, GL_LINEAR)));
        grassModel.getModelTexture().setNumberOfRows(2);
        treeModel = getLoader().loadToVAO(treeModelData.getVertices(), treeModelData.getIndices(),
                treeModelData.getTextureCoords(), treeModelData.getNormals());
        lowPolyTreeModel = new TexturedModel(getLoader().loadToVAO(lowPolyTree.getVertices(), lowPolyTree.getIndices(),
                lowPolyTree.getTextureCoords(), lowPolyTree.getNormals()),  new ModelTexture(getLoader().loadTexture("lowPolyTree", GL_RGBA,GL_NEAREST )));

        FIXME
         */
        backgroundTexture = new TerrainTexture(getLoader().loadTexture("grass3", GL_RGBA, GL_LINEAR));
        rTexture = new TerrainTexture(getLoader().loadTexture("mud", GL_RGBA, GL_LINEAR));
        gTexture = new TerrainTexture(getLoader().loadTexture("grassFlowers", GL_RGBA, GL_LINEAR));
        bTexture = new TerrainTexture(getLoader().loadTexture("path", GL_RGBA, GL_NEAREST));
        blendMap = new TerrainTexture(getLoader().loadTexture("blendMap", GL_RGBA, GL_NEAREST));


        texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);

        //terrain = new Terrain(0, 0, getLoader(), texturePack, blendMap, "heightMap");
        // FIXME treemodel = new TexturedModel(treeModel, new ModelTexture(getLoader().loadTexture("tree", GL_RGBA, GL_NEAREST)));

        // FIXME player = new Player(new TexturedModel(bunnyModel,new ModelTexture(getLoader().loadTexture("white", GL_RGBA, GL_LINEAR))),
        //        new Vector3f(300, 0, 600), 0, 0, 0, 1, terrain);
        texture = treemodel.getModelTexture();

        lights.add(new Light(new Vector3f(418, 100, 227), new Vector3f(255, 255, 255), new Vector3f(1, 0.01f, 0.02f)));
        //lights.add(new Light(new Vector3f(418,10, 227),new Vector3f(0,0,10), new Vector3f(1, 0.01f, 0.002f)));
        lights.add(new Light(new Vector3f(370,17,-300),new Vector3f(10,0,0), new Vector3f(1, 0.01f, 0.002f)));
        lights.add(new Light(new Vector3f(293, 7, -305), new Vector3f(2, 2, 0), new Vector3f(1, 0.01f, 0.002f)));
        ModelData lampModel = OBJFileLoader.loadOBJ("fern");
        // FIXME TexturedModel lamp = new TexturedModel(getLoader().loadToVAO(lampModel.getVertices(), lampModel.getIndices(),
               // lampModel.getTextureCoords(), lampModel.getNormals()), new ModelTexture(getLoader().loadTexture("lamp", GL_RGBA, 0)));

        //worldEntities.add(new thewall.engine.twilight.entity.Model(lamp, new Vector3f(293, 7, -305), 1f, terrain));
        //worldEntities.add(new thewall.engine.twilight.entity.Model(lamp, new Vector3f(370,17,-300), 1f, terrain));
        //worldEntities.add(new thewall.engine.twilight.entity.Model(lamp, new Vector3f(185,10, -293), 1f, terrain));
        //FIXME
        //player.hide();

        enableAutoWindowResizable();

        texture.setShineDamper(10);
        texture.setReflectivity(1);

        int seed = 0xFFFF;
        Random worldRandom = new Random(seed);

        for(int i = 0; i < 400; i++){
            int x = worldRandom.nextInt(50, 760 + 1);
            int z = worldRandom.nextInt(50, 760 + 1);
            // FIXME worldEntities.add(new RawEntity(grassModel, new Random().nextInt(4) ,new Vector3f(x, terrain.getHeightOfTerrain(x, z), z), 2, terrain));
        }

        // drzewa low poly
        for(int i = 0; i < 400; i++){
            int x = worldRandom.nextInt(50, 760 + 1);
            int z = worldRandom.nextInt(50, 760 + 1);
            float size = 0.2f + new Random().nextFloat() * (0.3f - 0.2f);
            // FIXME worldEntities.add(new RawEntity(lowPolyTreeModel, new Vector3f(x, terrain.getHeightOfTerrain(x, z), z), 1, terrain));
        }
        //ModelData wtcModelData = OBJFileLoader.loadOBJ("");
        //TexturedModel wtcModel = new TexturedModel(getLoader().loadToVAO(wtcModelData.getVertices(), wtcModelData.getIndices(),
        //        wtcModelData.getTextureCoords(), wtcModelData.getNormals()), new ModelTexture(getLoader().loadTexture("", GL_RGBA, GL_NEAREST)));
       // worldEntities.add(new Entity(wtcModel, new Vector3f(250, 0, 250), 0, 0, 0,1));

        getEventManager().registerEvents(new GamepadEvent());

        //guis.add(new GuiTexture(getLoader().loadTexture("pistole", GL_RGBA, GL_NEAREST), new Vector2f(0.5f, 0.5f), new Vector2f(0.25f, 0.25f)));
    }

    @Override
    public void onDisable() {

    }

    float[] lastWatchdog = new float[1000];
    int lastWatchdogIndex = 0;

    @SneakyThrows
    @Override
    public void update() {

        player.tick();

        // getRenderer().processEntity(player); FIXME

        getRenderer().processTerrain(terrain);

        for (Spatial ent : worldEntities) {
            getRenderer().processEntity(ent);
        }

        getRenderer().render(lights, player.getCamera());

        getGuiRenderer().render(guis);

        double currentTime = glfwGetTime();
        frameCount++;
        if (currentTime - previousTime >= 1.0) {
            long total = getRealtimeHardware().getMemory().getTotalAllocated() / MEGABYTE;
            long available = getRealtimeHardware().getMemory().getAvailableAllocated() / MEGABYTE;
            setWindowTitle("MEMORY: " + available + "MB "
                    + "/ " + total + "MB"
                    + " | FPS: " + frameCount + " | " + getRealtimeHardware().getProcessor().getName() + " | " + getRealtimeHardware().getUsedGraphic().getName());
            if (frameCount <= 30) {
                logger.warn("FPS drop detected, current framerate: " + frameCount);
            }

            fps = frameCount;

            frameCount = 0;
            previousTime = currentTime;

            if(fps > 20
                    && fps < 140){
                Thread.sleep(100000);
            }
        }

        if(lastWatchdogIndex == lastWatchdog.length - 1){
            //Arrays.fill(lastWatchdog, 0);
            lastWatchdogIndex = -1;
        }

        lastWatchdog[++lastWatchdogIndex] = (float) ((System.currentTimeMillis() - getWatchdog().getKeepAliveTime())/ 1000.0);


    }
    boolean isWindowSizeSet = false;

    float maxWatchdog = 0.005f;

    @OnImmediateGUI
    public void onImmediateGUI() {
        ImGuiDesigner gui = getImmediateGUI();
        Vector2i windowSize = getWindowSize();
        if(!isWindowSizeSet) {
            gui.beginWindow("Engine Stats");
            isWindowSizeSet = true;
        }else {
            gui.beginWindow("Engine Stats");
        }
        gui.text("Twilight " + getVersion());
        gui.text("Time: " + new SimpleDateFormat(    "yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
        gui.text("Window: " + windowSize.x + "x" + windowSize.y);
        gui.text("FPS: " + fps);
        gui.text("Watchdog time: " + (System.currentTimeMillis() - getWatchdog().getKeepAliveTime()) / 1000.0);

        float max_watchdog = maximum(lastWatchdog);

        if(max_watchdog > maxWatchdog){
            maxWatchdog = max_watchdog;
        }

        gui.plotLines("", lastWatchdog, 0, 0.020f, 200, 100);

        gui.endWindow();

    }

    @Contract(pure = true)
    private static float maximum(float @NotNull [] array) {
        if (array.length <= 0)
            throw new IllegalArgumentException("The array is empty");
        float max = array[0];
        for (int i = 1; i < array.length; i++)
            if (array[i] > max)
                max = array[i];
        return max;
    }

    private static long getMaxMemory() {
        return Runtime.getRuntime().maxMemory();
    }

    private static long getUsedMemory() {
        return getMaxMemory() - getFreeMemory();
    }

    private static long getTotalMemory() {
        return Runtime.getRuntime().totalMemory();
    }

    private static long getFreeMemory() {
        return Runtime.getRuntime().freeMemory();
    }

}
