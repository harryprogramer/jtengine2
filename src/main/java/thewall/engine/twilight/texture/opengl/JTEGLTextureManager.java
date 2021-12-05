package thewall.engine.twilight.texture.opengl;

import de.matthiasmann.twl.utils.PNGDecoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.checkerframework.common.value.qual.ArrayLenRange;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import thewall.engine.twilight.errors.TextureDecoderException;
import thewall.engine.twilight.renderer.opengl.GL;
import thewall.engine.twilight.renderer.opengl.GL2;
import thewall.engine.twilight.renderer.opengl.GL3;
import thewall.engine.twilight.texture.PixelFormat;
import thewall.engine.twilight.utils.Validation;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;


// TODO: optimizations
public final class JTEGLTextureManager implements GLTextureManager {
    private final static Logger logger = LogManager.getLogger(JTEGLTextureManager.class);
    private List<Integer> textures = new ArrayList<>();
    private float mipmappingLevel = -0.04f;
    private final GL gl;
    private final GL2 gl2;
    private final GL3 gl3;

    public JTEGLTextureManager(GL gl){
        this.gl = gl != null ? (GL) gl : null;
        this.gl2 = gl instanceof GL2 ? (GL2) gl : null;
        this.gl3 = gl instanceof GL3 ? (GL3) gl : null;

        String error = "";

        if(gl == null){
            error = "OpenGL 1.0+ is not supported";
        }else if(gl2 == null){
            error = "OpenGL 2.0+ is not supported";
        }else if(gl3 == null){
            error = "OpenGL 3.0+ is not supported";
        }

        if(gl == null || gl2 == null || gl3 == null){
            throw new RuntimeException(error);
        }
    }

    private int generateTexture(ByteBuffer buffer, int width, int height , @NotNull PixelFormat format, Map<GLTextureParameter, GLTextureFilter> parameters){
        int id = gl.glGenTextures();

        textures.add(id);

        gl.glBindTexture(gl.GL_TEXTURE_2D, id);
        gl.glPixelStorei(gl.GL_UNPACK_ALIGNMENT, 1);

        if(parameters != null && !parameters.isEmpty()) {
            for (Map.Entry<GLTextureParameter, GLTextureFilter> entry : parameters.entrySet()) {
                GLTextureParameter key = entry.getKey();
                GLTextureFilter tab = entry.getValue();
                gl.glTexParameteri(gl.GL_TEXTURE_2D, key.glCode, tab.glCode);
            }
        }

        gl.glTexImage2D(gl.GL_TEXTURE_2D, 0, format.getOpenGLCode(), width, height, 0, format.getOpenGLCode(), gl.GL_UNSIGNED_BYTE, buffer);

        gl3.glGenerateMipmap(gl3.GL_TEXTURE_2D);
        gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MIN_FILTER, gl.GL_LINEAR_MIPMAP_LINEAR);
        gl.glTexParameterf(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_LOD_BIAS, mipmappingLevel);
        return id;
    }

    private int generateTextureFromFile(String filename, PixelFormat pixelFormat, Map<GLTextureParameter, GLTextureFilter> parameters) throws IOException {
        PNGDecoder decoder = new PNGDecoder(new FileInputStream("res/texture/" + filename + ".png"));
        ByteBuffer buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
        PNGDecoder.Format format;
        switch (pixelFormat){
            case RGB -> format = PNGDecoder.Format.RGB;
            case RGBA -> format = PNGDecoder.Format.RGBA;
            default -> throw new TextureDecoderException("Not support pixel format [" + pixelFormat.name() + "]");
        }
        decoder.decode(buffer, decoder.getWidth() * 4, format);
        buffer.flip();

        return generateTexture(buffer, decoder.getWidth(), decoder.getHeight(), pixelFormat, parameters);
    }

    private int generateTextureFromFile(String filename, PixelFormat format) throws IOException {
        Map<GLTextureParameter, GLTextureFilter> parameters = new HashMap<>();
        parameters.put(GLTextureParameter.TEXTURE_MAG_FILTER, GLTextureFilter.LINEAR);
        parameters.put(GLTextureParameter.TEXTURE_MIN_FILTER, GLTextureFilter.LINEAR_MIPMAP_LINEAR);
        return generateTextureFromFile(filename, format, parameters);
    }

    private @NotNull TextureData decodeTextureFile(String fileName) throws TextureDecoderException {
        int width;
        int height;
        ByteBuffer buffer;
        logger.debug("Decoding texture [" + fileName + "]");
        try{
            FileInputStream in = new FileInputStream(fileName);
            PNGDecoder pngDecoder = new PNGDecoder(in);
            width = pngDecoder.getWidth();
            height = pngDecoder.getHeight();
            buffer = ByteBuffer.allocateDirect(4 * width * height);
            pngDecoder.decode(buffer, width * 4, PNGDecoder.Format.RGBA);
            buffer.flip();
            in.close();
        }catch (Exception e){
            logger.error("Texture file decoder error, cannot decode [" + fileName + "]", e);
            throw new TextureDecoderException(e);
        }

        return new TextureData(width, height, buffer);
    }

    private int loadCubeMap(@Range(from = 0, to = 5) String[] files, PixelFormat format) {
        Map<GLTextureParameter, GLTextureFilter> parameters = new HashMap<>();
        parameters.put(GLTextureParameter.TEXTURE_MAG_FILTER, GLTextureFilter.LINEAR);
        parameters.put(GLTextureParameter.TEXTURE_MIN_FILTER, GLTextureFilter.LINEAR);

        return loadCubeMap(files, parameters, format);
    }

    private int loadCubeMap(@Range(from = 0, to = 5) String @NotNull [] files, Map<GLTextureParameter, GLTextureFilter> parameters, PixelFormat format) {
        if(files.length != 6){
            logger.debug("Invaild 3D texture file array, " + Arrays.toString(files));
            throw new TextureDecoderException("Invalid files length array, excepted size [5], provided: [" + files.length + "]");
        }

        ByteBuffer[] buffers = new ByteBuffer[6];
        TextureData textureData = decodeTextureFile("res/texture/" + files[0]);
        buffers[0] = textureData.getBuffer();
        int width = textureData.getWidth(), height = textureData.getHeight();

        for(int i = 1; i < 6; i++){
            buffers[i] = decodeTextureFile("res/texture/" + files[i]).getBuffer();
        }

        return loadCubeMap(buffers, width, height, format, parameters);
    }

    private int loadCubeMap(ByteBuffer[] buffers, int width, int height, PixelFormat pixelFormat) {
        Map<GLTextureParameter, GLTextureFilter> parameters = new HashMap<>();
        parameters.put(GLTextureParameter.TEXTURE_MAG_FILTER, GLTextureFilter.LINEAR);
        parameters.put(GLTextureParameter.TEXTURE_MIN_FILTER, GLTextureFilter.LINEAR);

        return loadCubeMap(buffers, width, height, pixelFormat, parameters);
    }

    private int loadCubeMap(ByteBuffer[] buffers, int width, int height, PixelFormat pixelFormat, Map<GLTextureParameter, GLTextureFilter> parameters) {
        int texID = gl.glGenTextures();
        gl.glActiveTexture(gl.GL_TEXTURE0);
        gl.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);

        Validation.checkNull(getClass(), buffers);
        int coordinates = gl.GL_TEXTURE_CUBE_MAP_POSITIVE_X - 1;
        for(int i = 0; i < 6; i++){
            ByteBuffer buffer = buffers[i];
            GL11.glTexImage2D(++coordinates, 0, pixelFormat.getOpenGLCode(), width, height,
                    0, pixelFormat.getOpenGLCode(), gl.GL_UNSIGNED_BYTE, buffer);
        }

        if(parameters != null && !parameters.isEmpty()) {
            for (Map.Entry<GLTextureParameter, GLTextureFilter> entry : parameters.entrySet()) {
                GLTextureParameter key = entry.getKey();
                GLTextureFilter tab = entry.getValue();
                gl.glTexParameteri(gl.GL_TEXTURE_CUBE_MAP, key.glCode, tab.glCode);
            }
        }
        textures.add(texID);
        return texID;
    }

    @Override
    public int loadTexture(String filename) {
        return 0;
    }

    @Override
    public int loadTexture(String filename, PixelFormat format) {
        try {
            return generateTextureFromFile(filename, format);
        } catch (IOException e) {
            throw new TextureDecoderException(e);
        }
    }

    @Override
    public int loadTexture(String filename, PixelFormat format, Map<GLTextureParameter, GLTextureFilter> parameters) {
        try {
            return generateTextureFromFile(filename, format, parameters);
        }catch (IOException e){
            throw new TextureDecoderException(e);
        }
    }

    @Override
    public int loadTexture(ByteBuffer buffer, int width, int height, PixelFormat format) {
        Map<GLTextureParameter, GLTextureFilter> parameters = new HashMap<>();
        parameters.put(GLTextureParameter.TEXTURE_MAG_FILTER, GLTextureFilter.LINEAR);
        parameters.put(GLTextureParameter.TEXTURE_MIN_FILTER, GLTextureFilter.LINEAR_MIPMAP_LINEAR);
        return generateTexture(buffer, width, height, format, parameters);
    }

    @Override
    public int loadTexture(ByteBuffer buffer, int width, int height, PixelFormat format, Map<GLTextureParameter, GLTextureFilter> parameters) {
        return generateTexture(buffer, width, height, format, parameters);
    }

    @Override
    public int load3DTexture(ByteBuffer[] buffers, int width, int height, PixelFormat format, Map<GLTextureParameter, GLTextureFilter> parameters) {
        return loadCubeMap(buffers, width, height, format, parameters);
    }

    @Override
    public int load3DTexture(@ArrayLenRange(to = 5) String[] files) {
        return loadCubeMap(files, PixelFormat.RGBA);
    }

    @Override
    public int load3DTexture(@ArrayLenRange(to = 5) String[] files, PixelFormat format) {
        return loadCubeMap(files, format);
    }

    @Override
    public int load3DTexture(ByteBuffer[] buffers, int width, int height, PixelFormat format) {
        return loadCubeMap(buffers, width, height, format);
    }

    @Override
    public int load3DTexture(ByteBuffer[] buffers, int width, int height) {
        return loadCubeMap(buffers, width, height, PixelFormat.RGBA);
    }


    @Override
    public void setMipmappingLevel(float level) {
        this.mipmappingLevel = level;
    }

    @Override
    public float getMipmappingLevel() {
        return mipmappingLevel;
    }

    @Override
    public void freeTexture(int texture) {
        if(texture == -1 || texture == 0){
            throw new NullPointerException("Texture is null");
        }
        gl.glDeleteTextures(texture);
    }

    @Override
    public void cleanUp() {
        for(Integer texture : textures){
            gl.glDeleteTextures(texture);
        }
    }
}
