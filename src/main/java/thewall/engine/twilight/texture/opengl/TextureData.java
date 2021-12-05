package thewall.engine.twilight.texture.opengl;

import java.nio.ByteBuffer;

import static thewall.engine.twilight.utils.Validation.checkNull;

final class TextureData {
    private final int width;
    private final int height;
    private final ByteBuffer buffer;

    protected TextureData(int width, int height, ByteBuffer buffer){
        checkNull(buffer, getClass());
        checkNull(width, getClass());
        checkNull(height, getClass());
        this.width = width;
        this.height = height;
        this.buffer = buffer;
    }

    /**
     * Get the width of decoded image
     * @return texture width
     */
    public int getWidth(){
        return width;
    }

    /**
     * Get the height of decoded image
     * @return texture height
     */
    public int getHeight(){
        return height;
    }

    /**
     * Get the buffer of decoded image
     * @return
     */
    public ByteBuffer getBuffer(){
        return buffer;
    }
}
