package jte2.engine.twilight.texture;

import jte2.engine.twilight.utils.Validation;

public class Texture2D extends AbstractTexture {
    private final Picture picture;

    public Texture2D(PixelFormat pixelFormat, int width, int height, Picture texture) {
        super(pixelFormat, width, height);
        Validation.checkNull(texture);
        this.picture = texture;
    }

    public Picture getTexture(){
        return picture;
    }
}
