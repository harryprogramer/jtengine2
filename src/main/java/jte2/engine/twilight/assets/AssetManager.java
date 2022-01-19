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

    Texture loadTexture(InputStream inputStream, PixelFormat format);

    /**
     * File [1]: Right Face<p>
     * File [2]: Left Face<p>
     * File [3]: Top Face<p>
     * File [4]: Bottom Face<p>
     * File [5]: Back Face<p>
     * File [6]: Front Face
     * @param filename array of files to cube texture
     * @return 3d texture
     */
    Texture loadTexture3D(String[] filename);

    Texture loadTexture3D(String[] filename, PixelFormat format);

    Spatial loadModel(String filename);

}
