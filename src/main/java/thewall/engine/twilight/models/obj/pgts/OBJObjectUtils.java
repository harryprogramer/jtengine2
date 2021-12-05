package thewall.engine.twilight.models.obj.pgts;

import org.joml.Vector2f;
import org.joml.Vector3f;
import thewall.engine.twilight.errors.NotImplementedException;
import thewall.engine.twilight.models.Loader;
import thewall.engine.twilight.models.Mesh;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class OBJObjectUtils {

    @Deprecated(forRemoval = true)
    @Contract(pure = true)
    public static Mesh loadToRawModel(@NotNull OBJObject objObject, Loader loader){
        List<Float> vertexPosition = new ArrayList<>();
        List<Float> texturePosition = new ArrayList<>();
        List<Integer> faceList = new ArrayList<>();

        List<Integer> vertexIncides = new ArrayList<>();
        List<Float> vertexTexturePositionProcessed = new ArrayList<>();

        for(Vector3f pos : objObject.getVertexPositions()){
            vertexPosition.add(pos.x);
            vertexPosition.add(pos.y);
            vertexPosition.add(pos.z);
        }

        for(Vector2f vector2f : objObject.getTextureCoordinates()){
            texturePosition.add(vector2f.x);
            texturePosition.add(vector2f.y);
        }

        for(ObjectFace face : objObject.getModelFaces()){
            for(VertexCoord vertexCoord : face.getVertexCoords()){
                vertexIncides.add(vertexCoord.getVector());
                vertexTexturePositionProcessed.add(texturePosition.get(vertexCoord.getTextureCoordinate()));
            }
        }
        // FIXME
        //return loader.loadToVAO(listToFloatArray(vertexPosition), listToIntegerArray(vertexIncides), listToFloatArray(vertexTexturePositionProcessed), null);
        throw new NotImplementedException();
    }

    @Contract(pure = true)
    private static float @NotNull [] listToFloatArray(@NotNull List<Float> floats){
        float[] floatArray = new float[floats.size()];
        int i = 0;

        for (Float f : floats) {
            floatArray[i++] = (f != null ? f : Float.NaN);
        }

        return floatArray;
    }

    @Contract(pure = true)
    private static int[] listToIntegerArray(@NotNull List<Integer> integers){
        return integers.stream().mapToInt(i->i).toArray();
    }
}
