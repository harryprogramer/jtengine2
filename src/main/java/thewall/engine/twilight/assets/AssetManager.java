package thewall.engine.twilight.assets;

import thewall.engine.twilight.entity.Spatial;
import thewall.engine.twilight.material.Material;
import thewall.engine.twilight.texture.PixelFormat;
import thewall.engine.twilight.texture.Texture;

import java.io.InputStream;
import java.nio.ByteBuffer;

public interface AssetManager {

    Texture loadTexture(String filename);

    Texture loadTexture(ByteBuffer buffer, int width, int height, PixelFormat format);

    Texture loadTexture(InputStream inputStream, int width, int height, PixelFormat format);

    Spatial loadModel(String filename);

}
