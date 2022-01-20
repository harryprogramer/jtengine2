package jte2.engine.twilight.texture;

import jte2.engine.twilight.Area;
import jte2.engine.twilight.errors.TextureDecoderException;
import jte2.engine.twilight.utils.Validation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.checkerframework.common.value.qual.ArrayLenRange;
import org.jetbrains.annotations.NotNull;

public class Texture3D extends Texture {
    private final static Logger logger = LogManager.getLogger(Texture3D.class);

    private final Picture[] cubeTextures;

    public enum Face {
        RIGHT,
        LEFT,
        FRONT,
        BACK,
        UP,
        DOWN
    }

    public Texture3D(PixelFormat pixelFormat, int width, int height, Picture @NotNull [] pictures) {
        super(pixelFormat, width, height);
        if(pictures.length != 6){
            throw new IllegalStateException("incorrect pictures array");
        }

        validatePictures(pictures);
        this.cubeTextures = pictures;
    }

    private static void validatePictures(Picture @NotNull [] pictures){
        Area resolution = pictures[0].getResolution();
        for(int i = 1; i < 6; i++){
            if(pictures[i] == null){
                logger.error("Provided 3D Texture has null picture buffer at index [{}]", i);
                throw new NullPointerException("texture at index [" + i + "] is null");
            }else {
                if (!pictures[i].getResolution().equals(resolution)) {
                    throw new TextureDecoderException("cube texture must have the same resolution in all pictures");
                }
            }
        }
    }

    public Picture[] getTextures(){
        return cubeTextures;
    }

    public Picture getFace(@NotNull Face face){
        switch (face){
            case RIGHT -> {
                return cubeTextures[0];
            }

            case LEFT -> {
                return cubeTextures[1];
            }

            case FRONT -> {
                return cubeTextures[2];
            }

            case BACK -> {
                return cubeTextures[3];
            }

            case UP -> {
                return cubeTextures[4];
            }

            case DOWN -> {
                return cubeTextures[5];
            }

            default -> throw new IllegalStateException("invalid face");
        }
    }



}
