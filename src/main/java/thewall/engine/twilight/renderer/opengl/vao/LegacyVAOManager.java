package thewall.engine.twilight.renderer.opengl.vao;

import com.google.common.collect.Lists;
import com.google.common.primitives.Floats;
import com.google.common.primitives.Ints;
import lombok.SneakyThrows;
import oms3.annotations.In;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.Integers;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.BufferUtils;
import thewall.engine.twilight.models.Mesh;
import thewall.engine.twilight.renderer.opengl.GL;
import thewall.engine.twilight.renderer.opengl.GL2;
import thewall.engine.twilight.renderer.opengl.GL3;
import thewall.engine.twilight.utils.Validation;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.*;

public class LegacyVAOManager implements VAOManager {
    private final static Logger logger = LogManager.getLogger(LegacyVAOManager.class);
    private final Map<Integer, List<Integer>> fbo = new HashMap<>();

    private final GL gl;
    private final GL2 gl2;
    private final GL3 gl3;

    public LegacyVAOManager(GL gl){
        this.gl = gl != null ? (GL) gl : null;
        this.gl2 = gl instanceof GL2 ? (GL2) gl : null;
        this.gl3 = gl instanceof GL3 ? (GL3) gl : null;

        String error = "";

        if(gl == null){
            error = "OpenGL 1.0+ must be supported";
        }else if(gl2 == null){
            error = "OpenGL 2.0+ must be supported";
        }else if(gl3 == null){
            error = "OpenGL 3.0+ must be supported";
        }

        if(gl == null || gl2 == null || gl3 == null){
            throw new RuntimeException(error);
        }
    }

    private void putVBO(int vao, int vbo){
        List<Integer> vbos = fbo.get(vao);
        vbos.add(vbo);
        fbo.replace(vao, vbos);
    }

    private void putVAO(int vao){
        fbo.put(vao, new ArrayList<>());
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

    private void storeDataInVAO(int number, int coordinateSize ,float[] data, int vao){
        int vboID = gl.glGenBuffers();
        putVBO(vao, vboID);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboID);
        FloatBuffer floatBuffer = storeDataInFloatBuffer(data);
        gl.glBufferData(GL.GL_ARRAY_BUFFER, floatBuffer, GL.GL_STATIC_DRAW);
        gl2.glVertexAttribPointer(number, coordinateSize, GL.GL_FLOAT, false, 0, 0);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
    }

    private void bindIndicesBuffer(int[] indices){
        int vboID = gl2.glGenBuffers();
        putVAO(vboID);
        gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer intBuffer = storeDataInIntBuffer(indices);
        gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, intBuffer, GL.GL_STATIC_DRAW);
    }

    private void freeUpVAO(int vao){
        gl3.glDeleteVertexArrays(vao);
    }

    private int createVAO(){
        int vaoID = gl3.glGenVertexArrays();
        putVAO(vaoID);
        gl3.glBindVertexArray(vaoID);
        return vaoID;
    }

    private void unbindVAO(){
        gl3.glBindVertexArray(0);
    }

    @SneakyThrows
    private int loadMeshToVAO(float[] vertices, int[] indices, float[] textureCoordinates, float[] geometryNormals){
        PrintWriter fileOutputStream = new PrintWriter(new FileOutputStream("models.txt"));
        int iMax = vertices.length - 1;
        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0; ; i++) {
            b.append(vertices[i]);
            if (i == iMax) {
                fileOutputStream.println("Vertices: " + b.append(']'));
                break;
            }
            b.append("f, ");
        }
        //fileOutputStream.println("Vertices: " + Arrays.toString(Floats.toArray(mesh.getVertices())));
        fileOutputStream.println("Indices: " + Arrays.toString(indices));
        iMax = textureCoordinates.length - 1;
        b = new StringBuilder();
        b.append('[');
        for (int i = 0; ; i++) {
            b.append(textureCoordinates[i]);
            if (i == iMax) {
                fileOutputStream.println("Texture: " + b.append(']'));
                break;
            }
            b.append("f, ");
        }
        iMax = geometryNormals.length - 1;
        b = new StringBuilder();
        b.append('[');
        for (int i = 0; ; i++) {
            b.append(geometryNormals[i]);
            if (i == iMax) {
                fileOutputStream.println("Normals: " + b.append(']'));
                break;
            }
            b.append("f, ");
        }
        fileOutputStream.flush();
        int vao = createVAO();
        bindIndicesBuffer(indices);
        storeDataInVAO(0, 3, vertices, vao);
        storeDataInVAO(1, 2, textureCoordinates, vao);
        storeDataInVAO(2, 3, geometryNormals, vao);
        unbindVAO();
        return vao;
    }

    @Override
    public int loadToVAO(Mesh mesh) {
        Validation.checkNull(mesh);
        return loadToVAO(mesh.getVertices(), mesh.getIndices(), mesh.getTextureCoordinates(), mesh.getNormals());
    }

    @Override
    public int loadToVAO(float[] vertices, int[] indices, float[] textureCoordinates, float[] geometryNormals) {
        return loadMeshToVAO(vertices, indices, textureCoordinates, geometryNormals);
    }

    @Override
    public int loadToVAO(List<Float> vertices, List<Integer> indices, List<Float> textureCoordinates, List<Float> geometryNormals) {
        return loadToVAO(Floats.toArray(vertices), Ints.toArray(indices), Floats.toArray(textureCoordinates), Floats.toArray(geometryNormals));
    }

    @Override
    public int loadToVAO(float[] positions, int dimensions) {
        int vaoID = createVAO();
        storeDataInVAO(0, dimensions, positions, vaoID);
        unbindVAO();
        return vaoID;
    }

    @Override
    public void freeMesh(@NotNull Mesh mesh) {
        List<Integer> vbos = fbo.get(mesh.getID());
        if(vbos == null){
            throw new NullPointerException("VAO not exist");
        }
        gl3.glDeleteVertexArrays(mesh.getID());
        for(Integer vbo : vbos){
            gl.glDeleteBuffers(vbo);
        }
        fbo.remove(mesh.getID());
    }

    @Override
    public void cleanUp() {
        for(Map.Entry<Integer, List<Integer>> entry : fbo.entrySet()){
            Integer vao = entry.getKey();
            List<Integer> vbos = entry.getValue();
            gl3.glDeleteVertexArrays(vao);
            for(Integer vbo : vbos){
                gl.glDeleteBuffers(vbo);
            }
        }
    }
}
