package thewall.engine.twilight.renderer.opengl.vao;

import org.jetbrains.annotations.Contract;
import thewall.engine.twilight.models.Mesh;

import java.util.List;

public interface VAOManager {
    @Contract(pure = true)
    int loadToVAO(Mesh mesh);

    @Contract(pure = true)
    int loadToVAO(float[] vertices, int[] indices, float[] textureCoordinates, float[] geometryNormals);

    @Contract(pure = true)
    int loadToVAO(List<Float> vertices, List<Integer> indices, List<Float> textureCoordinates, List<Float> geometryNormals);

    @Contract(pure = true)
    int loadToVAO(float[] positions, int dimensions);

    void freeMesh(Mesh mesh);

    void cleanUp();
}
