package jte2.engine.twilight.texture;

import jte2.engine.twilight.Area;
import jte2.engine.twilight.errors.NotImplementedException;
import jte2.engine.twilight.errors.TextureDecoderException;
import jte2.engine.twilight.utils.Validation;
import net.sf.image4j.codec.ico.ICODecoder;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.swt.widgets.IME;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.List;

public class Picture {
    private final static Logger logger = LogManager.getLogger(Picture.class);
    private ByteBuffer buffer;
    private BufferedImage image;
    private PixelFormat format;
    private Area resolution;

    public enum ImageFormat {
        JPEG,
        ICO,
        PNG,
        JPG,
        BMP
    }

    public Picture(BufferedImage buffer, PixelFormat format){
        this.resolution = new Area(buffer.getWidth(), buffer.getHeight());
        this.buffer = convertImageData(buffer, format);
        this.format = format;
        this.image = buffer;
    }

    public Picture(String filename){
        this(filename, PixelFormat.RGBA);
    }

    public Picture(String filename, PixelFormat format){
        Validation.checkNull(filename);
        loadPicture(filename, format);
        if(image == null) {
            throw new TextureDecoderException("cannot read picture [" + filename + "], with " + format.name());
        }
        this.resolution = new Area(image.getWidth(), image.getHeight());
    }

    private ImageFormat resolveFormat(Iterator<ImageReader> iter, String filename){
        String imageFormat = null;
        try {
            imageFormat = iter.next().getFormatName().toUpperCase();
        }catch (Exception e){
            logger.info("No metadata was found in image [{}], a primitive method will be used.", filename);
        }
        if(imageFormat == null){
            imageFormat = FilenameUtils.getExtension(filename).toUpperCase();
        }
        ImageFormat format;
        switch (imageFormat){
            case "PNG" -> format = ImageFormat.PNG;
            case "ICO" -> format = ImageFormat.ICO;
            case "JPEG" -> format = ImageFormat.JPEG;
            case "JPG" -> format = ImageFormat.JPG;
            case "BMP" -> format = ImageFormat.BMP;
            default -> throw new TextureDecoderException("unsupported format: " + imageFormat);
        }
        return format;

    }

    @Contract(pure = true)
    private BufferedImage loadByFormat(@NotNull ImageFormat format, File image) throws IOException {
        switch (format){
            case JPEG, BMP, PNG -> {
                return ImageIO.read(image);
            }

            case JPG -> {
                throw NotImplementedException.NOT_IMPLEMENTED;
            }

            case ICO -> {
                List<BufferedImage> icos = ICODecoder.read(image);
                return icos.get(icos.size() - 1);
            }

            default -> throw new TextureDecoderException("no supported format: " + format.name());
        }
    }

    private void loadPicture(String filename, PixelFormat format){
        File file = new File("res/texture/" + filename);
        if(!file.exists()){
            file = new File(filename);
            if(!file.exists()){
                logger.error("Cannot find image (texture) [{}] at {}", filename, System.getProperty("user.dir"));
                throw new TextureDecoderException("cannot find picture " + filename);
            }
        }
        try {
            ImageInputStream iis = ImageIO.createImageInputStream(file);
            Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
            ImageFormat imageFormat = resolveFormat(iter, file.getName());
            logger.info("Resolved file format for [{}] is {}", file.getName(), format);
            BufferedImage image = loadByFormat(imageFormat, file);
            logger.info("Decoding image [{}] with format [{}], metadata: {}", file.getName(), format.name(), iter.hasNext() ? iter : null);
            this.resolution = new Area(image.getWidth(), image.getHeight());
            this.buffer = convertImageData(image, format);
            this.format = format;
            this.image = image;
        }catch (Exception e){
            e.printStackTrace();
            logger.warn("Cannot decode image [{}], {}", filename, e.getMessage());
            throw new TextureDecoderException(e);
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
