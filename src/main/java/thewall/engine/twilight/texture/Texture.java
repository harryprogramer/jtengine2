package thewall.engine.twilight.texture;

import java.nio.ByteBuffer;

public class Texture {
    private final PixelFormat pixelFormat;
    private final ByteBuffer buffer;
    private int textureAtlasSize = 1;
    private int textureAtlasIndex = 0;

    private final int textureWidth, textureHeight;
    private boolean isTransparency = false, isFakeLighting = false;

    private float shineDamper, reflectivity;

    public Texture(ByteBuffer buffer, PixelFormat pixelFormat, int width, int height){
        this.pixelFormat = pixelFormat;
        this.buffer = buffer;
        this.textureWidth = width;
        this.textureHeight = height;
    }

    public Texture(ByteBuffer buffer, PixelFormat pixelFormat, int width, int height, int textureAtlasSize){
        this(buffer, pixelFormat, width, height);
        this.textureAtlasSize = textureAtlasSize;
    }

    public void setTextureAtlasIndex(int index){
        this.textureAtlasIndex = index;
    }

    public int getTextureAtlasSize(){
        return textureAtlasSize;
    }

    public ByteBuffer getTextureBuffer(){
        return buffer;
    }

    public int getTextureBufferSize(){
        return buffer.capacity();
    }

    public int getTextureWidth(){
        return textureWidth;
    }

    public int getTextureHeight(){
        return textureHeight;
    }

    public PixelFormat getPixelFormat(){
        return pixelFormat;
    }

    public void setTransparency(boolean transparency){
        this.isTransparency = transparency;
    }

    public void setFakeLighting(boolean fakeLighting){
        this.isFakeLighting = fakeLighting;
    }

    public boolean isTransparency(){
        return isTransparency;
    }

    public boolean isFakeLighting(){
        return isFakeLighting;
    }

    public float getShineDamper(){
        return shineDamper;
    }

    public void setShineDamper(float shineDamper){
        this.shineDamper = shineDamper;
    }

    public float getReflectivity(){
        return reflectivity;
    }

    public void setReflectivity(float reflectivity){
        this.reflectivity = reflectivity;
    }

    public int getTextureAtlasIndex() {
        return textureAtlasIndex;
    }
}
