package thewall.engine.twilight.material;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.BufferUtils;
import thewall.engine.twilight.texture.PixelFormat;
import thewall.engine.twilight.texture.Texture;
import thewall.engine.twilight.utils.Colour;
import thewall.engine.twilight.utils.Validation;

import java.nio.ByteBuffer;

public class Material {
    private final static Logger logger = LogManager.getLogger(Material.class);

    private int x, y, id = -1;
    private ByteBuffer materialBuffer;
    private PixelFormat materialFormat;
    private final String name;
    private boolean transparency = true,  isFakeLighting = false;
    private int textureIndex = 1;

    private int multiTextureRows = 1;

    private float shineDamper = 1, reflectivity = 0;

    public Material(String name){
        Validation.checkNull(name);
        this.name = name;
    }

    /*
    public ByteBuffer loadTexture(InputStream filename, PixelFormat format) throws IOException {
        PNGDecoder pngDecoder;
        try{
            pngDecoder = new PNGDecoder(filename);
        }catch (IOException e){
            throw new TextureDecoderException(e);
        }

        // stworz bufor z 4 wartosciami o matrycy x na y gdzie x to wysokosc a y szerokosc
        ByteBuffer buffer = BufferUtils.createByteBuffer(format.getSize() * pngDecoder.getWidth() * pngDecoder.getHeight());

        PNGDecoder.Format pngFormat;
        switch (format){
            case RGBA -> pngFormat = PNGDecoder.Format.RGBA;
            case RGB -> pngFormat = PNGDecoder.Format.RGB;
            default -> throw new TextureDecoderException("Invaild or unsupported image format [" + format.name() + "]");
        }

        pngDecoder.decode(buffer, 4 * pngDecoder.getWidth(), pngFormat);

        buffer.flip();

        this.x = pngDecoder.getWidth();
        this.y = pngDecoder.getHeight();

        this.materialFormat = format;

        return buffer;
    }

     */


    public Material loadColour(@NotNull Colour colour){
        ByteBuffer buffer = BufferUtils.createByteBuffer(PixelFormat.RGBA.getSize());
        buffer.put((byte) colour.getRed());
        buffer.put((byte) colour.getGreen());
        buffer.put((byte) colour.getBlue());
        buffer.put((byte) colour.getAlpha());
        buffer.flip();

        this.materialBuffer = buffer;
        this.materialFormat = PixelFormat.RGBA;
        this.x = 1;
        this.y = 1;

        return this;
    }

    public void setTexture(Texture texture){
        Validation.checkNull(texture);
        this.materialBuffer = texture.getTextureBuffer();
        this.x = texture.getTextureWidth();
        this.y = texture.getTextureHeight();
        this.isFakeLighting = texture.isFakeLighting();
        this.transparency = texture.isTransparency();
        this.shineDamper = texture.getShineDamper();
        this.multiTextureRows = texture.getTextureAtlasSize();
        this.reflectivity = texture.getReflectivity();
        this.textureIndex = texture.getTextureAtlasIndex();
        /*
        try {
            ByteBuffer byteBuffer = loadTexture(new FileInputStream(filename), PixelFormat.RGBA);
            if(byteBuffer.capacity() == 0){
                throw new StackOverflowError("buffer is zero");
            }
            this.materialBuffer = byteBuffer;


        } catch (Exception e) {
            throw new TextureDecoderException(e);
        }

         */
    }

    public void setTransparency(boolean transparency){
        this.transparency = transparency;
    }

    public boolean isTransparency(){
        return transparency;
    }

    public void setID(int id){
        if(id == -1 || id == 0){
            throw new IllegalStateException("Invalid texture (material) ID.");
        }
        this.id = id;
    }

    public int getID() {
        return id;
    }

    public ByteBuffer getMaterialBuffer(){
        return materialBuffer;
    }

    public int getMaterialWidth(){
        return x;
    }

    public int getMaterialHeight(){
        return y;
    }

    public PixelFormat getMaterialFormat(){
        return materialFormat;
    }

    public float getShineDamper(){
        return shineDamper;
    }

    public float getReflectivity(){
        return reflectivity;
    }

    public boolean isFakeLighting(){
        return isFakeLighting;
    }

    public int getMultiTextureRows(){
        return multiTextureRows;
    }

    public String getName() {
        return name;
    }

    public float getTextureXOffset(){
        int column = textureIndex % multiTextureRows;
        return (float) column / (float) multiTextureRows;
    }

    public float getTextureYOffset(){
        int row = textureIndex / multiTextureRows;
        return (float) row / (float) multiTextureRows;
    }


}
