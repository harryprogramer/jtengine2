package thewall.engine.twilight.material;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.BufferUtils;
import thewall.engine.twilight.shaders.ShaderHandle;
import thewall.engine.twilight.texture.PixelFormat;
import thewall.engine.twilight.texture.Texture;
import thewall.engine.twilight.utils.Validation;

import java.nio.ByteBuffer;

public class Material {
    private final static Logger logger = LogManager.getLogger(Material.class);

    private static int index = -1;
    private int x, y, id = -1;
    private ByteBuffer materialBuffer;
    private PixelFormat materialFormat;
    private final String name;
    private boolean transparency = true,  isFakeLighting = false, is3D = false;
    private int textureIndex = 1;
    private ShaderHandle shader = null;

    private int multiTextureRows = 1;

    private float shineDamper = 1, reflectivity = 0;

    public Material(String name){
        Validation.checkNull(name);
        this.name = name;
        index++;
    }

    public Material(ShaderHandle shader){
        this();
        this.shader = shader;
    }

    public Material(String name, ShaderHandle shader){
        this(name);
        this.shader = shader;
    }

    public Material(){
        this.name = "Material-" + ++index;
    }

    /**
     * If custom shader is set to this material, this function will return it.
     * If no shader is set, return value is null.
     * @return shader
     */
    public ShaderHandle getShader() {
        return shader;
    }

    /**
     * Set custom shader to this material.
     * If custom shader is set, renderer should be skip his default shader and load custom material shader.
     * @param shader shader
     */
    public void setShader(ShaderHandle shader){
        Validation.checkNull("Shader", shader);
        this.shader = shader;
    }

    /**
     * Bind {@link Colour} to material.
     * @param colour color
     */
    public void setColour(@NotNull Colour colour){
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
    }

    /**
     * Load color to material and return it.
     * @param colour color
     * @deprecated
     * @return color
     */
    @Deprecated
    public Material loadColour(@NotNull Colour colour){
        setColour(colour);
        return this;
    }

    /**
     * Bind texture to this material
     * @param texture texture
     */
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
        this.materialFormat = texture.getPixelFormat();
        this.is3D = texture.isTexture3D();
        this.id = -1;
    }

    public void set3D(boolean is3D){
        this.is3D = is3D;
    }

    public boolean is3D(){
        return is3D;
    }

    /**
     * Set is material transparency
     * @param transparency is transparency
     */
    public void setTransparency(boolean transparency){
        this.transparency = transparency;
    }

    /**
     * Is material transparency
     * @return transparency
     */
    public boolean isTransparency(){
        return transparency;
    }

    /**
     * Set native material object id
     * @param id object id
     */
    public void setID(int id){
        if(id == -1 || id == 0){
            throw new IllegalStateException("Invalid texture (material) ID.");
        }
        this.id = id;
    }

    /**
     * Get native material object id
     * @return id
     */
    public int getID() {
        return id;
    }

    /**
     * Get material buffer
     * @return buffer
     */
    public ByteBuffer getMaterialBuffer(){
        return materialBuffer;
    }

    /**
     * Get material width
     * @return width
     */
    public int getMaterialWidth(){
        return x;
    }

    /**
     * Get material height.
     * If static color is loaded, material height has set 1 height.
     * If texture is loaded, material height is set to texture height;
     * @return height
     */
    public int getMaterialHeight(){
        return y;
    }

    /**
     * Get material pixel format.
     * If static color is loaded, format is set to {@link PixelFormat#RGBA}
     * @see #getMaterialBuffer()
     * @return material buffer format
     */
    public PixelFormat getMaterialFormat(){
        return materialFormat;
    }

    /**
     * Get material shine damper value.
     * @return shine damper
     */
    public float getShineDamper(){
        return shineDamper;
    }

    /**
     * Get material reflectivity
     * @return reflectivity
     */
    public float getReflectivity(){
        return reflectivity;
    }

    /**
     * Is material use fake lighting
     * @return fake lighting
     */
    public boolean isFakeLighting(){
        return isFakeLighting;
    }

    /**
     * Get indexs of multitexture rows if texture is set.
     * Default value is 0.
     * @return multitexture rows
     */
    public int getMultiTextureRows(){
        return multiTextureRows;
    }

    /**
     * Get name of material
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Get texture X offset
     * @return offset
     */
    public float getTextureXOffset(){
        int column = textureIndex % multiTextureRows;
        return (float) column / (float) multiTextureRows;
    }

    /**
     * Get texture Y offset
     * @return offset
     */
    public float getTextureYOffset(){
        int row = textureIndex / multiTextureRows;
        return (float) row / (float) multiTextureRows;
    }


}
