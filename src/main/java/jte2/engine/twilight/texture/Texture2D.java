package jte2.engine.twilight.texture;

import jte2.engine.twilight.utils.Validation;

import java.awt.image.BufferedImage;

public class Texture2D extends Texture {
    private final Picture picture;
    private BufferedImage heightMap;
    private BufferedImage normalMap;
    private int id = -1;

    private boolean isHeightMap = false, isNormalMap = false;

    public Texture2D(Picture texture) {
        super(texture.getFormat(), texture.getWidth(), texture.getHeight());
        Validation.checkNull(texture);
        this.picture = texture;
    }

    public Picture getTexture(){
        return picture;
    }


    public boolean isNormalMapping(){
        return isNormalMap;
    }

    public boolean isHeightMap(){
        return isHeightMap;
    }

    public void setHeightMap(BufferedImage image){
        Validation.checkNull(image);
        this.heightMap = image;
    }

    public void setNormalMap(BufferedImage image){
        Validation.checkNull(image);
        this.normalMap = image;
    }

    public void setHeightMap(boolean heightMap){
        if(this.heightMap == null && heightMap){
            this.heightMap = convertToHeight(picture.getImage());
        }
        this.isHeightMap = heightMap;
    }

    public void setNormalMap(boolean normalMap){
        if(this.normalMap == null && normalMap){
            if(heightMap == null){
                this.heightMap = convertToHeight(getTexture().getImage());
            }
            this.normalMap = convertToNormal(heightMap, 5.0f);
        }
        this.isNormalMap = normalMap;
    }

}
