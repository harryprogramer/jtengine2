package thewall.engine.twilight.texture;

import lombok.Getter;
import lombok.Setter;

@Deprecated
public final class ModelTexture {
    private final int textureID;

    private float shineDamper = 1;
    private float reflectivity = 0;
    @Setter
    @Getter
    private boolean hasTransparency = false;

    @Getter
    @Setter
    private boolean useFakeLighting = false;

    @Setter
    @Getter
    private int numberOfRows = 1;

    public ModelTexture(int texture){
        this.textureID = texture;
    }

    public int getID(){
        return textureID;
    }

    public float getShineDamper() {
        return shineDamper;
    }

    public void setShineDamper(float shineDamper) {
        this.shineDamper = shineDamper;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }

}
