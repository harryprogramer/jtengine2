package thewall.engine.twilight.assets;

import thewall.engine.twilight.entity.Spatial;
import thewall.engine.twilight.texture.PixelFormat;
import thewall.engine.twilight.texture.Texture;

import java.io.InputStream;
import java.nio.ByteBuffer;

// TODO
public class DesktopAssetManager implements AssetManager{
    @Override
    public Texture loadTexture(String filename) {
        return null;
    }

    @Override
    public Texture loadTexture(ByteBuffer buffer, int width, int height, PixelFormat format) {
        return null;
    }

    @Override
    public Texture loadTexture(InputStream inputStream, int width, int height, PixelFormat format) {
        return null;
    }

    @Override
    public Spatial loadModel(String filename) {
        return null;
    }
}
