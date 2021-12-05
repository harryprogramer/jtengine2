package thewall.engine.twilight.texture.opengl;

import org.checkerframework.common.value.qual.ArrayLenRange;
import org.jetbrains.annotations.Range;
import thewall.engine.twilight.texture.PixelFormat;

import java.nio.ByteBuffer;
import java.util.Map;

public interface GLTextureManager {
    // TODO: docs
    int loadTexture(String filename);

    /**
     * Load 2D texture from file
     * @param filename filename of image
     * @param format pixel format to load
     * @return OpenGL texture id
     */
    int loadTexture(String filename, PixelFormat format);

    /**
     * Load 2D texture from file
     * @param filename filename of image
     * @param format pixel format to load
     * @param parameters additional parameters for texture param
     * @return OpenGL texture id
     */
    int loadTexture(String filename, PixelFormat format, Map<GLTextureParameter, GLTextureFilter> parameters);

    /**
     * Load 2D texture from buffer
     * @param buffer texture buffer
     * @param width texture width
     * @param height texture height
     * @param format pixel format to load
     * @return OpenGL texture id
     */
    int loadTexture(ByteBuffer buffer, int width, int height, PixelFormat format);

    /**
     * Load 2D texture from buffer
     * @param buffer texture buffer
     * @param width texture width
     * @param height texture height
     * @param format pixel format to load
     * @param parameters additonal parameters for texture param
     * @return OpenGL texture id
     */
    int loadTexture(ByteBuffer buffer, int width, int height, PixelFormat format, Map<GLTextureParameter, GLTextureFilter> parameters);

    // TODO: docs
    int load3DTexture(ByteBuffer[] buffers, int width, int height, PixelFormat format, Map<GLTextureParameter, GLTextureFilter> parameters);

    /**
     * Load 3D texture
     * @param files name of panorama files
     * @return OpenGL texture id
     */
    int load3DTexture(@ArrayLenRange(to = 5) String[] files);

    /**
     * Load 3D texture
     * @param files name of panorama files
     * @param format image format
     * @return OpenGL texture id
     */
    int load3DTexture(@ArrayLenRange(to = 5) String[] files, PixelFormat format);

    // TODO: docs
    int load3DTexture(ByteBuffer[] buffers, int width, int height, PixelFormat format);

    // TODO: docs
    int load3DTexture(ByteBuffer[] buffers, int width, int height);

    /**
     * Set mipmapping level for next texture load
     * @param level mipmapping level
     */
    void setMipmappingLevel(float level);

    /**
     * Get the current used mipmapping level
     * @return mipmapping level
     */
    float getMipmappingLevel();

    /**
     * Clean up specify texture
     * @param texture OpenGL texture id
     */
    void freeTexture(int texture);

    /**
     * Clean all textures
     */
    void cleanUp();
}
