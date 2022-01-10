package jte2.engine.twilight.assets;

import de.matthiasmann.twl.utils.PNGDecoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import jte2.engine.twilight.errors.TextureDecoderException;
import jte2.engine.twilight.material.Colour;
import jte2.engine.twilight.material.Material;
import jte2.engine.twilight.models.Mesh;
import jte2.engine.twilight.models.obj.thinmatrix.OBJFileLoader;
import jte2.engine.twilight.spatials.Spatial;
import jte2.engine.twilight.texture.PixelFormat;
import jte2.engine.twilight.texture.Texture;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class DesktopAssetManager implements AssetManager {
    private static final Logger logger = LogManager.getLogger(DesktopAssetManager.class);

    private ByteBuffer loadTextureBuffer(PNGDecoder decoder, PixelFormat format) throws IOException {
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

    @Override
    public Texture loadTexture(String filename, PixelFormat format) {
        ByteBuffer buffer;

        PNGDecoder decoder;
        try {
            FileInputStream fileInputStream = new FileInputStream("res/texture/" + filename);
            decoder = new PNGDecoder(fileInputStream);
            buffer = loadTextureBuffer(decoder, format);
        }catch (Exception e){
            logger.error("Cannot load texture buffer for [" + filename + "]", e);
            throw new TextureDecoderException(e);
        }

        return new Texture(buffer, format, decoder.getWidth(), decoder.getHeight());
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

        return new Texture(buffer, format, width, height);
    }

    @Override
    public Texture loadTexture(InputStream inputStream, int width, int height, PixelFormat format) {
        if(inputStream == null){
            throw new NullPointerException("InputStream is null");
        }

        ByteBuffer buffer;
        PNGDecoder pngDecoder;
        try{
            pngDecoder = new PNGDecoder(inputStream);
            buffer = loadTextureBuffer(pngDecoder, format);
        }catch (Exception e){
            logger.error("Cannot load texture from custom byte stream", e);
            throw new TextureDecoderException(e);
        }

        return new Texture(buffer, format, pngDecoder.getWidth(), pngDecoder.getHeight());
    }

    @Override
    public Spatial loadModel(String filename) {
        Mesh mesh = OBJFileLoader.loadOBJMesh(filename);
        return new Model(mesh);
    }
}
