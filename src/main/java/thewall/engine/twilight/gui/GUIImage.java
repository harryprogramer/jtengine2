package thewall.engine.twilight.gui;

import org.joml.Vector2f;
import thewall.engine.twilight.material.Material;
import thewall.engine.twilight.texture.Texture;

public class GUIImage extends GUIComponent {
    private Material material;

    public GUIImage(Texture texture, Vector2f transformation, float scaleX, float scaleY){
        if(texture == null){
            throw new NullPointerException("Texture is null");
        }

        if(transformation == null){
            throw new NullPointerException("Transformation is null.");
        }
        setTransformation(transformation);
        setScale(new Vector2f(scaleX, scaleY));
        changeImage(texture);
    }

    public void changeImage(Texture texture){
        Material material = new Material();
        material.setTexture(texture);
        this.material = material;
    }

    @Override
    public Material getContent() {
        return material;
    }
}
