package jte2.engine.twilight.texture;

import de.matthiasmann.twl.utils.PNGDecoder;
import jte2.engine.twilight.Area;
import jte2.engine.twilight.errors.TextureDecoderException;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.nio.ByteBuffer;

public class Texture3D extends AbstractTexture {
    private final Picture[] cubeTextures;

    private static final String[] TEXTURE_FILES = {"skybox/right.png", "skybox/left.png", "skybox/top.png",
            "skybox/bottom.png", "skybox/back.png", "skybox/front.png"};

    public Texture3D(PixelFormat pixelFormat, int width, int height, Picture @NotNull [] pictures) {
        super(pixelFormat, width, height);
        if(pictures.length != 5){
            throw new IllegalStateException("incorrect pictures array");
        }

        validatePictures(pictures);
        this.cubeTextures = pictures;
    }

    private static void validatePictures(Picture @NotNull [] pictures){
        Area resolution = pictures[0].getResolution();
        for(int i = 1; i < 5; i++){
            if(!pictures[i].getResolution().equals(resolution)){
                throw new TextureDecoderException("cube texture must have the same resolution in all pictures");
            }
        }
    }

    public Picture getRight(){
        return cubeTextures[0];
    }

    public Picture getLeft(){
        return cubeTextures[1];
    }

    public Picture getFront(){
        return cubeTextures[2];
    }

    public Picture getBack(){
        return cubeTextures[3];
    }

    public Picture getUp(){
        return cubeTextures[4];
    }

    public Picture getDown(){
        return cubeTextures[5];
    }
}
