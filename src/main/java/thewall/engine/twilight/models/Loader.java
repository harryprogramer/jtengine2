package thewall.engine.twilight.models;


import com.google.common.primitives.Floats;
import com.google.common.primitives.Ints;
import de.matthiasmann.twl.utils.PNGDecoder;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryStack;
import thewall.engine.twilight.errors.NotImplementedException;
import thewall.engine.twilight.errors.TextureDecoderException;
import thewall.engine.twilight.utils.Validation;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.stb.STBImage.*;

@Deprecated(forRemoval = true)
public class Loader {
    private final static Logger logger = LogManager.getLogger(Loader.class);

    private final List<Integer> vaos = new ArrayList<>();
    private final List<Integer> vbos = new ArrayList<>();
    private final List<Integer> textures = new ArrayList<>();

    public Object /*FIXME*/ loadToVAO(Mesh model){
        int vaoID = createVAO();

        bindIndicesBuffer(Ints.toArray(model.getIndices()));
        storeDataInAttributeList(0,3, Floats.toArray(model.getVertices()));
        storeDataInAttributeList(1,2, Floats.toArray(model.getTextureCoordinates()));
        storeDataInAttributeList(2,3, Floats.toArray(model.getNormals()));
        unbindVAO();
        // return new VAOModelBuilder(vaoID, model.getIndices().size());
        throw new NotImplementedException(); /*FIXME*/
    }

    public VAOModel loadToVAO(float[] positions){
        return loadToVAO(positions, 2);
    }

    public VAOModel loadToVAO(float[] positions, int dimensions){
        int vaoID = createVAO();
        this.storeDataInAttributeList(0, dimensions, positions);
        unbindVAO();
        //return new VAOModelBuilder(vaoID, positions.length / dimensions);
        throw new NotImplementedException(); /*FIXME*/
    }

    private int createVAO(){
        int vaoID = GL30.glGenVertexArrays();
        vaos.add(vaoID);
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }

    @SneakyThrows
    private int loadTexture5(String filename, int pixelFormat){
        //reserved
        return 0;
    }

    public int loadCubeMap(String @NotNull [] textureFiles){
        int texID = GL11.glGenTextures();
        GL13.glActiveTexture(GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);

        Validation.checkNull(getClass(), textureFiles);
        int coordinates = GL_TEXTURE_CUBE_MAP_POSITIVE_X - 1;
        for(String string : textureFiles){
            //TextureData textureData = decodeTextureFile("res/texture/skybox/" + string + ".png");
            //GL11.glTexImage2D(++coordinates, 0, GL_RGBA, textureData.getWidth(), textureData.getHeight(),
            //        0, GL_RGBA, GL_UNSIGNED_BYTE, textureData.getBuffer());
        }

        GL11.glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        GL11.glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        textures.add(texID);
        return texID;
    }






    @SneakyThrows
    private int loadTexture4(String filename, int pixelFormat){

        //load png file
        PNGDecoder decoder = new PNGDecoder(new FileInputStream("res/texture/" + filename + ".png"));

        //create a byte buffer big enough to store RGBA values
        ByteBuffer buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());

        //decode
        decoder.decode(buffer, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);

        //flip the buffer so its ready to read
        buffer.flip();

        //create a texture
        int id = glGenTextures();

        //bind the texture
        glBindTexture(GL_TEXTURE_2D, id);

        //tell opengl how to unpack bytes
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        //set the texture parameters, can be GL_LINEAR or GL_NEAREST
        /*
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, textureMagFilter); // Linear Filtering
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, textureMagFilter); // Linear Filter

         */
        glTexParameteri (GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR); // brak możliwości trójliniowości przy powiększeniu
        glTexParameteri (GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR); // trójliniowy, gdy rozdzielczość ekranu jest niższa niż rozdzielczość tex
        //upload texture
        glTexImage2D(GL_TEXTURE_2D, 0, pixelFormat, decoder.getWidth(), decoder.getHeight(), 0, pixelFormat, GL_UNSIGNED_BYTE, buffer);

        // Generate Mip Map
        GL30.glGenerateMipmap(GL_TEXTURE_2D);
        GL11.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        GL11.glTexParameterf(GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -0.04f);
        return id;

    }


    private int loadTexture3(String filename, int pixelFormat){
        // Generate texture on GPU
        int texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);

        // Set texture parameters
        // Repeat image in both directions
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        // When stretching the image, pixelate
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        // When shrinking an image, pixelate
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        ByteBuffer image = stbi_load("res/texture/" + filename + ".png", width, height, channels, 0);

        if (image != null) {
            if (channels.get(0) == 3) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0), height.get(0),
                        0, GL_RGB, GL_UNSIGNED_BYTE, image);
            } else if (channels.get(0) == 4) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0),
                        0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            } else {
                assert false : "Error: (Texture) Unknown number of channels '" + channels.get(0) + "'";
            }
        } else {
            assert false : "Error: (Texture) Could not load image '" + filename + "'";
        }

        stbi_image_free(image);

        return texID;
    }


    private int loadTexture2(String filename, int pixelFormat){
        int width;
        int height;
        ByteBuffer buffer;
        try (MemoryStack stack = MemoryStack.stackPush()){
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            File file = new File("res/texture/" + filename + ".png");
            String filePath = file.getAbsolutePath();
            buffer = stbi_load(filePath, w, h, channels, 4);
            if(buffer ==null) {
                throw new Exception(stbi_failure_reason());
            }
            width = w.get();
            height = h.get();

            int id = GL11.glGenTextures();
            textures.add(id);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL_RGBA16, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
            stbi_image_free(buffer);
            return id;
        } catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int loadTexture(String fileName, int pixelFormat, @Deprecated int textureMagFilter){

        return loadTexture4(fileName, pixelFormat);
    }

    private void storeDataInAttributeList(int number, int coordinateSize ,float[] data){
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer floatBuffer = storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, floatBuffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(number, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private void unbindVAO(){
        GL30.glBindVertexArray(0);
    }

    private void bindIndicesBuffer(int[] indices){
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer intBuffer = storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, intBuffer, GL15.GL_STATIC_DRAW);
    }

    private @NotNull IntBuffer storeDataInIntBuffer(int @NotNull [] data){
        IntBuffer intBuffer = BufferUtils.createIntBuffer(data.length);
        intBuffer.put(data);
        intBuffer.flip();
        return intBuffer;
    }

    private @NotNull FloatBuffer storeDataInFloatBuffer(float @NotNull [] data){
        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(data.length);
        floatBuffer.put(data);
        floatBuffer.flip();
        return floatBuffer;
    }

    public void cleanUp(){
        for(Integer vao : vaos){
            GL30.glDeleteVertexArrays(vao);
        }

        for(Integer vbo : vbos){
            GL15.glDeleteBuffers(vbo);
        }

        for(Integer texture : textures){
            GL11.glDeleteTextures(texture);
        }
    }

}
