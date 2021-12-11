package thewall.engine.twilight.viewport;

import thewall.engine.twilight.material.Colour;
import thewall.engine.twilight.spatials.Camera;
import thewall.engine.twilight.spatials.Light;
import thewall.engine.twilight.utils.Validation;

import java.util.ArrayList;
import java.util.List;

public class ViewPort2D {
    private final RenderQueue2D renderQueue = new RenderQueue2D() ;
    private Colour backgroundColour = Colour.AQUA;

    private static int VIEWPORT_NAME_INDEX = 0;
    private Camera camera = new Camera();
    private List<Light> lights;
    private String name;

    public ViewPort2D(String name){
        Validation.checkNull(name);
        this.name = name;
        this.lights = new ArrayList<>();
    }

    public ViewPort2D(){
        this("ViewPort2D-" + ++VIEWPORT_NAME_INDEX);
    }

    public void attachScene(Node2D node){
        renderQueue.add(node);
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

    public RenderQueue2D getRenderQueue(){
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
