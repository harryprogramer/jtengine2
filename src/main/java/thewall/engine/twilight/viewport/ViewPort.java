package thewall.engine.twilight.viewport;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thewall.engine.twilight.material.Colour;
import thewall.engine.twilight.spatials.Camera;
import thewall.engine.twilight.spatials.Light;
import thewall.engine.twilight.spatials.Spatial;
import thewall.engine.twilight.utils.Validation;

import java.util.ArrayList;
import java.util.List;

public final class ViewPort {
    private final RenderQueue renderQueue = new RenderQueue();
    private Colour backgroundColour = Colour.AQUA;
    private static int VIEWPORT_NAME_INDEX = 0;
    private Camera camera = new Camera();
    private Spatial skybox = null;
    private final List<Light> lights;
    private String name;

    public ViewPort(String name){
        Validation.checkNull(name);
        this.name = name;
        this.lights = new ArrayList<>();
    }

    public ViewPort(){
        this("ViewPort-" + ++VIEWPORT_NAME_INDEX);
    }

    public void attachSkybox(@NotNull Spatial spatial){
        if(!spatial.getMaterial().is3D()){
            throw new IllegalArgumentException("Texture is not 3D.");
        }
        this.skybox = spatial;
    }

    public @Nullable Spatial getSkybox(){
        return this.skybox;
    }

    public void attachScene(Node node){
        renderQueue.add(node);
    }

    public void detachScene(Node node){
        renderQueue.remove(node);
    }

    public void setCamera(Camera camera){
        Validation.checkNull(camera);
        this.camera = camera;
    }

    public Camera getCamera(){
        return camera;
    }

    public List<Light> getLights(){
        return lights;
    }

    public void deleteLight(int index){
        lights.remove(index);
    }

    public void deleteLight(Light light){
        lights.remove(light);
    }


    public Light getLight(int index){
        if(index > lights.size()){
            throw new IndexOutOfBoundsException("Index out of bounds, lights size: " + lights.size());
        }
        return lights.get(index);
    }

    public void addLight(Light light){
        Validation.checkNull(light);
        lights.add(light);
    }

    public void setName(String name){
        this.name = name;
    }

    public RenderQueue getRenderQueue(){
        return renderQueue;
    }

    public String getName(){
        return name;
    }

    /**
     * Get background color of viewport
     * @return background color
     */
    public Colour getBackgroundColor() {
        return backgroundColour;
    }

    /**
     * Set the color of the background
     * @param color background color
     */
    public void setBackgroundColor(Colour color){
        if(color == null){
            throw new NullPointerException("Color is null");
        }
        this.backgroundColour = color;
    }
}
