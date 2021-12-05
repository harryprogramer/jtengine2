package thewall.engine.twilight;

import thewall.engine.twilight.entity.Spatial;
import thewall.engine.twilight.material.Material;
import thewall.engine.twilight.models.Mesh;
import thewall.engine.twilight.utils.SafeArrayList;
import thewall.engine.twilight.utils.Validation;

import java.util.ArrayList;
import java.util.List;

public class Node extends Spatial{
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
