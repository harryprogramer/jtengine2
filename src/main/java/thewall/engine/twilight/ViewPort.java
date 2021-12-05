package thewall.engine.twilight;

import org.apache.spark.internal.config.R;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import thewall.engine.twilight.entity.Camera;
import thewall.engine.twilight.entity.Light;
import thewall.engine.twilight.entity.Spatial;
import thewall.engine.twilight.utils.Validation;

import javax.swing.text.View;
import java.util.ArrayList;
import java.util.List;

public final class ViewPort implements Cloneable{
    private final RenderQueue renderQueue = new RenderQueue();

    private static int VIEWPORT_NAME_INDEX = 0;
    private List<Light> lights;
    private String name;
    private Camera camera;

    public ViewPort(String name){
        Validation.checkNull(name);
        this.name = name;
        this.lights = new ArrayList<>();
    }

    public ViewPort(){
        this("ViewPort-" + ++VIEWPORT_NAME_INDEX);
    }

    public void attachScene(Node node){
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

    public RenderQueue getRenderQueue(){
        return renderQueue;
    }

    public String getName(){
        return name;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone(); // TODO
    }
}
