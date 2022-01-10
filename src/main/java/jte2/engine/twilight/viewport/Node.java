package jte2.engine.twilight.viewport;

import jte2.engine.twilight.spatials.Spatial;
import jte2.engine.twilight.material.Material;
import jte2.engine.twilight.utils.SafeArrayList;
import jte2.engine.twilight.utils.Validation;

import java.util.List;

public class Node extends Spatial {
    private final SafeArrayList<Spatial> children = new SafeArrayList<>(Spatial.class);

    public void attachChildAt(Spatial spatial, int index){
        Validation.checkNull(spatial);
        children.add(index, spatial);
    }

    public void attachChild(Spatial spatial){
        attachChildAt(spatial, children.size());
    }

    public void detachAllChildren(){
        for(int i = 0; i < children.size(); i++){
            detachChildAt(i);
        }
    }

    public Spatial getChild(int index){
        return children.get(index);
    }

    public List<Spatial> getChildren(){
        return children;
    }

    public void detachChildAt(int index){
        children.remove(index);
    }

    public void detachChild(Spatial spatial){
        children.remove(spatial);
    }

    @Override
    public void setMaterial(Material material) {
        for(Spatial spatial : children){
            spatial.setMaterial(material);
        }
    }
}
