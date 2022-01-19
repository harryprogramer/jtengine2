package jte2.engine.twilight.texture.opengl;

import org.checkerframework.common.value.qual.ArrayLenRange;
import jte2.engine.twilight.texture.PixelFormat;

import java.nio.ByteBuffer;
import java.util.Map;

public interface GLTextureManager {
    // TODO: docs
    int loadTexture(String filename);

    /**
     * Directly decodes the texture and loads it into OpenGL memory,
     * unlike {@link jte2.engine.twilight.assets.AssetManager#loadTexture(String)}
     * which only decodes a photo.
     * @param filename filename of image
     * @param format pixel format to load
     * @return OpenGL texture id
     */
    int loadTexture(String filename, PixelFormat format);

    /**
     * Directly decodes the texture and loads it into OpenGL memory,
     * unlike {@link jte2.engine.twilight.assets.AssetManager#loadTexture(String)}
     * which only decodes a photo.
     * Load 2D texture from file
     * @param filename filename of image
     * @param format pixel format to load
     * @param parameters additional parameters for texture param
     * @return OpenGL texture id
     */
    int loadTexture(String filename, PixelFormat format, Map<GLTextureParameter, GLTextureFilter> parameters);

    /**
     * Directly decodes the texture and loads it into OpenGL memory,
     * unlike {@link jte2.engine.twilight.assets.AssetManager#loadTexture(String)}
     * which only decodes a photo.
     * Load 2D texture from buffer
     * @param buffer texture buffer
     * @param width texture width
     * @param height texture height
     * @param format pixel format to load
     * @return OpenGL texture id
     */
    int loadTexture(ByteBuffer buffer, int width, int height, PixelFormat format);

    /**
     * Directly decodes the texture and loads it into OpenGL memory,
     * unlike {@link jte2.engine.twilight.assets.AssetManager#loadTexture(String)}
     * which only decodes a photo.
     * Load 2D texture from buffer
     * @param buffer texture buffer
     * @param width texture width
     * @param height texture height
     * @param format pixel format to load
     * @param parameters additional parameters for texture param
     * @return OpenGL texture id
     */
    int loadTexture(ByteBuffer buffer, int width, int height, PixelFormat format, Map<GLTextureParameter, GLTextureFilter> parameters);

    // TODO: docs
    int load3DTexture(ByteBuffer[] buffers, int width, int height, PixelFormat format, Map<GLTextureParameter, GLTextureFilter> parameters);

    /**
     * Directly decodes the texture and loads it into OpenGL memory,
     * unlike {@link jte2.engine.twilight.assets.AssetManager#loadTexture(String)}
     * which only decodes a photo.
     * Load 3D texture
     * @param files name of panorama files
     * @return OpenGL texture id
     */
    int load3DTexture(@ArrayLenRange(to = 5) String[] files);

    /**
     * Directly decodes the texture and loads it into OpenGL memory,
     * unlike {@link jte2.engine.twilight.assets.AssetManager#loadTexture(String)}
     * which only decodes a photo.
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
