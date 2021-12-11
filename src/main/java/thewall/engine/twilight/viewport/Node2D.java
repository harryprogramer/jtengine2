package thewall.engine.twilight.viewport;

import thewall.engine.twilight.material.Material;
import thewall.engine.twilight.spatials.Spatial;
import thewall.engine.twilight.spatials.Spatial2D;
import thewall.engine.twilight.utils.SafeArrayList;
import thewall.engine.twilight.utils.Validation;

import java.util.List;

public class Node2D extends Spatial2D {
    private final SafeArrayList<Spatial2D> children = new SafeArrayList<>(Spatial2D.class);

    public void attachChildAt(Spatial2D spatial, int index){
        Validation.checkNull(spatial);
        children.add(index, spatial);
    }

    public void attachChild(Spatial2D spatial){
        attachChildAt(spatial, children.size());
    }

    public void detachAllChildren(){
        for(int i = 0; i < children.size(); i++){
            detachChildAt(i);
        }
    }

    public Spatial2D getChild(int index){
        return children.get(index);
    }

    public List<Spatial2D> getChildren(){
        return children;
    }

    public void detachChildAt(int index){
        children.remove(index);
    }

    public void detachChild(Spatial2D spatial){
        children.remove(spatial);
    }

    @Override
    public void setMaterial(Material material) {
        for(Spatial2D spatial : children){
            spatial.setMaterial(material);
        }
    }
}
