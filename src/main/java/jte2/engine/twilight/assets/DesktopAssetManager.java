package jte2.engine.twilight.assets;

import de.matthiasmann.twl.utils.PNGDecoder;
import jte2.engine.twilight.errors.TextureDecoderException;
import jte2.engine.twilight.material.Colour;
import jte2.engine.twilight.material.Material;
import jte2.engine.twilight.models.Mesh;
import jte2.engine.twilight.models.obj.thinmatrix.OBJFileLoader;
import jte2.engine.twilight.spatials.Spatial;
import jte2.engine.twilight.texture.*;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DesktopAssetManager implements AssetManager {
    private final Map<String, Object> assetsCache = new ConcurrentHashMap<>();
    private static final int BYTES_PER_PIXEL = 4;//3 for RGB, 4 for RGBA
    private static final Logger logger = LogManager.getLogger(DesktopAssetManager.class);

    private @NotNull ByteBuffer loadTextureBuffer(@NotNull PNGDecoder decoder, @NotNull PixelFormat format) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
        PNGDecoder.Format pngFormat;
        switch (format){
            case RGB -> pngFormat =     PNGDecoder.Format.RGB;
            case ALPHA -> pngFormat =   PNGDecoder.Format.ALPHA;
            default -> pngFormat =      PNGDecoder.Format.RGBA;
        }
        decoder.decode(buffer, decoder.getWidth() * 4, pngFormat);
        buffer.flip();

        return buffer;
    }

    private static class Model extends Spatial {
        public Model(Mesh mesh){
            Material material = new Material();
            material.setColour(Colour.WHITE);
            setMesh(mesh);
            setMaterial(material);
        }
    }

    @Override
    public Texture loadTexture(String filename) {
        return loadTexture(filename, PixelFormat.RGBA);
    }

    @SneakyThrows
    @Override
    public Texture loadTexture(String filename, PixelFormat format) {
        if(assetsCache.get(filename) != null){
            try {
                logger.info("Asset [{}] will be retrieved from the cache." + filename);
                return (Texture) assetsCache.get(filename);
            }catch (ClassCastException e){
                logger.warn("Invalid cache for asset [{}], object cannot be cast to texture", filename);
                assetsCache.remove(filename);
            }
        }

        BufferedImage image = ImageIO.read(new File("res/texture/" + filename));
        Picture picture = new Picture(image, format);
        Texture2D texture = new Texture2D(picture);
        logger.info("Loading texture asset [{}] with [{}] ", filename, String.format("%d/%d %s",
                picture.getResolution().getWidth(),
                picture.getResolution().getHeight(),
                picture.getFormat().name()));
        assetsCache.put(filename, texture);
        return texture;
    }

    @Override
    public Texture loadTexture(ByteBuffer buffer, int width, int height, PixelFormat format) {
        if(buffer == null){
            throw new NullPointerException("Buffer is null");
        }

        if(width < 0 || height < 0){
            throw new TextureDecoderException("Invalid texture size " + String.format("[W: %d, H: %d]", width, height));
        }

        if(format == null){
            throw new NullPointerException("Format is null");
        }
        return null;
        //return new Texture2D(buffer, format, width, height); TODO
    }

    @Override
    public Texture loadTexture(InputStream inputStream, PixelFormat format) {
        if(inputStream == null){
            throw new NullPointerException("InputStream is null");
        }

        BufferedImage image;
        try{
            image = ImageIO.read(inputStream);
        }catch (Exception e){
            logger.error("Cannot load texture from custom byte stream", e);
            throw new TextureDecoderException(e);
        }

        return new Texture2D(new Picture(image, format));
    }

    @Override
    public Texture loadTexture3D(String[] filename) {
        return loadTexture3D(filename, PixelFormat.RGBA);
    }

    @Override
    public Texture loadTexture3D(String[] filename, PixelFormat format) {
        if(filename.length != 6){
            throw new IllegalStateException("invalid filenames array length, excepted 5 but got " + filename.length);
        }
        Picture[] textures = new Picture[6];
        for(int i = 0; i < 6; i++){
            try {
                textures[i] = ((Texture2D) loadTexture(filename[i], PixelFormat.RGBA)).getTexture();
            }catch (Exception e){
                logger.error("Can't read texture [{}-{}], [{}]", i, filename[i], e.getMessage());
                textures[i] = ((Texture2D) loadTexture("res/texture/static.png", PixelFormat.RGBA)).getTexture();
            }
        }
        return new Texture3D(PixelFormat.RGBA,
                textures[0].getWidth(),
                textures[0].getHeight(),
                textures);
    }

    @Override
    public Spatial loadModel(String filename) {
        Mesh mesh = OBJFileLoader.loadOBJMesh(filename);
        return new Model(mesh);
    }
}
