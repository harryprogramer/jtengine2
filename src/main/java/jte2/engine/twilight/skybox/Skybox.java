package jte2.engine.twilight.skybox;

import jte2.engine.twilight.material.Material;
import jte2.engine.twilight.spatials.Spatial;
import jte2.engine.twilight.texture.Texture;
import jte2.engine.twilight.texture.Texture3D;

/**
 * Object that represents Skybox in universe.
 */
public class Skybox extends Spatial {
    public Skybox(Texture texture){
        if(!(texture instanceof Texture3D)){
            throw new IllegalStateException("texture is not 3d");
        }

        Material material = new Material();
        material.setTexture(texture);
        setMaterial(material);
    }
}
