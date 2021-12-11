package thewall.engine.twilight.gui;

import org.joml.Vector2f;
import thewall.engine.twilight.material.Material;
import thewall.engine.twilight.spatials.Spatial;
import thewall.engine.twilight.spatials.Spatial2D;

public abstract class GUIComponent extends Spatial2D {
    private final Vector2f scale = new Vector2f(1, 1);

    public Vector2f getScale(){
        return scale;
    }

    public void setScale(Vector2f scale){
        if(scale == null){
            throw new NullPointerException("Scale is null");
        }

        this.scale.set(scale);
    }

    public abstract Material getContent();
}
