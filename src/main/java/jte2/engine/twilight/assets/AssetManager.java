package jte2.engine.twilight.assets;

import jte2.engine.twilight.spatials.Spatial;
import jte2.engine.twilight.texture.PixelFormat;
import jte2.engine.twilight.texture.Texture;

import java.io.InputStream;
import java.nio.ByteBuffer;

public interface AssetManager {
    Texture loadTexture(String filename);

    Texture loadTexture(String filename, PixelFormat format);

    Texture loadTexture(ByteBuffer buffer, int width, int height, PixelFormat format);

    Texture loadTexture(InputStream inputStream, int width, int height, PixelFormat format);

    Spatial loadModel(String filename);

}
