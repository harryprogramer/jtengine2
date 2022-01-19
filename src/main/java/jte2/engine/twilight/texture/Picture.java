package jte2.engine.twilight.texture;

import jte2.engine.twilight.Area;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Picture {
    private final BufferedImage image;
    private final ByteBuffer buffer;
    private final PixelFormat format;
    private final Area resolution;
    private final int x, y;

    public Picture(BufferedImage buffer, int width, int height, PixelFormat format){
        this.resolution = new Area(width, height);
        this.buffer = convertImageData(buffer);
        this.format = format;
        this.image = buffer;
        this.x = width;
        this.y = height;
    }

    /**
     * Get {@link BufferedImage} of current picture
     * @return buffered picture
     */
    public BufferedImage getImage(){
        return image;
    }

    /**
     * Get picture {@link Picture#x} width
     * @return width
     */
    public int getWidth(){
        return x;
    }

    /**
     * Get picture {@link Picture#y} height
     * @return width
     */
    public int getHeight(){
        return y;
    }

    /**
     * Get picture {@link Picture#resolution} in {@link Area}
     * @return picture resolution in {@link Area}
     */
    public Area getResolution(){
        return resolution;
    }

    /**
     * Get picture format {@link PixelFormat}
     * @return pixel format
     */
    public PixelFormat getFormat() {
        return format;
    }

    /**
     * Get picture as {@link ByteBuffer}
     * @return {@link ByteBuffer} of image
     */
    public ByteBuffer getImageBuffer() {
        return buffer;
    }

    /* Thanks to https://stackoverflow.com/questions/29301838/converting-bufferedimage-to-bytebuffer */
    private static @NotNull ByteBuffer convertImageData(@NotNull BufferedImage bi)
    {
        byte[] pixelData = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
        ByteBuffer buf = ByteBuffer.allocateDirect(pixelData.length);
        buf.order(ByteOrder.nativeOrder());
        buf.put(pixelData);
        buf.flip();
        return buf;
    }

}
