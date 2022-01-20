package jte2.engine.twilight.texture;

import jte2.engine.twilight.Area;
import jte2.engine.twilight.utils.Validation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;

public class Picture {
    private final static Logger logger = LogManager.getLogger(Picture.class);
    private ByteBuffer buffer;
    private BufferedImage image;
    private PixelFormat format;
    private Area resolution;

    public Picture(BufferedImage buffer, PixelFormat format){
        this.resolution = new Area(buffer.getWidth(), buffer.getHeight());
        this.buffer = convertImageData(buffer, format);
        this.format = format;
        this.image = buffer;
    }

    public Picture(String filename){
        Validation.checkNull(filename);
        loadPicture(filename, PixelFormat.RGBA);
    }

    public Picture(String filename, PixelFormat format){
        Validation.checkNull(filename);
        loadPicture(filename, format);
    }

    public void loadPicture(String filename, PixelFormat format){
        try {
            BufferedImage image = ImageIO.read(new File("res/texture/" + filename));
            this.resolution = new Area(image.getWidth(), image.getHeight());
            this.buffer = convertImageData(image, format);
            this.format = format;
            this.image = image;
        }catch (Exception e){
            logger.warn("Cannot decode image [{}], {}", filename, e.getMessage());
        }

    }

    /**
     * Get {@link BufferedImage} of current picture
     * @return buffered picture
     */
    public BufferedImage getImage(){
        return image;
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

    public int getWidth(){
        return resolution.getWidth();
    }

    public int getHeight(){
        return resolution.getHeight();
    }

    private static @NotNull ByteBuffer convertImageData(@NotNull BufferedImage image, @NotNull PixelFormat format) {
        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * format.getSize()); //4 for RGBA, 3 for RGB

        for(int y = 0; y < image.getHeight(); y++){
            for(int x = 0; x < image.getWidth(); x++){
                int pixel = pixels[y * image.getWidth() + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));     // Red component
                buffer.put((byte) ((pixel >> 8) & 0xFF));      // Green component
                buffer.put((byte) (pixel & 0xFF));               // Blue component
                buffer.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component. Only for RGBA
            }
        }

        buffer.flip(); //FOR THE LOVE OF GOD DO NOT FORGET THIS
        return buffer;
    }

}
