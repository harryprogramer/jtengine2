package thewall.engine.twilight.spatials;

import com.google.common.primitives.Floats;
import com.google.common.primitives.Ints;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import thewall.engine.twilight.models.Mesh;

import java.util.ArrayList;

public class Box extends Spatial {
    private final static Logger logger = LogManager.getLogger(Box.class);
    Mesh boxMesh = new Mesh();

    public final Vector3f center = new Vector3f(0f, 0f, 0f);
    public float xExtent, yExtent, zExtent;

    private static final float[] GEOMETRY_NORMALS_DATA = {
            0,  0, -1,  0,  0, -1,  0,  0, -1,  0,  0, -1, // back
            1,  0,  0,  1,  0,  0,  1,  0,  0,  1,  0,  0, // right
            0,  0,  1,  0,  0,  1,  0,  0,  1,  0,  0,  1, // front
            -1,  0,  0, -1,  0,  0, -1,  0,  0, -1,  0,  0, // left
            0,  1,  0,  0,  1,  0,  0,  1,  0,  0,  1,  0, // top
            0, -1,  0,  0, -1,  0,  0, -1,  0,  0, -1,  0  // bottom
    };

    private final static float[] VERTICES = {
            -0.5f,0.5f,0,
            -0.5f,-0.5f,0,
            0.5f,-0.5f,0,
            0.5f,0.5f,0,

            -0.5f,0.5f,1,
            -0.5f,-0.5f,1,
            0.5f,-0.5f,1,
            0.5f,0.5f,1,

            0.5f,0.5f,0,
            0.5f,-0.5f,0,
            0.5f,-0.5f,1,
            0.5f,0.5f,1,

            -0.5f,0.5f,0,
            -0.5f,-0.5f,0,
            -0.5f,-0.5f,1,
            -0.5f,0.5f,1,

            -0.5f,0.5f,1,
            -0.5f,0.5f,0,
            0.5f,0.5f,0,
            0.5f,0.5f,1,

            -0.5f,-0.5f,1,
            -0.5f,-0.5f,0,
            0.5f,-0.5f,0,
            0.5f,-0.5f,1
    };

    private final static int[] INDICES = {
            2,  1,  0,  3,  2,  0, // back
            6,  5,  4,  7,  6,  4, // right
            10,  9,  8, 11, 10,  8, // front
            14, 13, 12, 15, 14, 12, // left
            18, 17, 16, 19, 18, 16, // top
            22, 21, 20, 23, 22, 20  // bottom
    };


    private final static float[] TEXTURE_COORDS = {
            0,0,
            0,1,
            1,1,
            1,0,
            0,0,
            0,1,
            1,1,
            1,0,
            0,0,
            0,1,
            1,1,
            1,0,
            0,0,
            0,1,
            1,1,
            1,0,
            0,0,
            0,1,
            1,1,
            1,0,
            0,0,
            0,1,
            1,1,
            1,0

    };

    public Box(float xExtent, float yExtent, float zExtent) {
        this.xExtent = xExtent;
        this.yExtent = yExtent;
        this.zExtent = zExtent;
        setScale(25);
        updateGeometry(center, xExtent, yExtent, zExtent);
    }

    protected final thewall.engine.twilight.math.Vector3f @NotNull [] computeVertices() {
        thewall.engine.twilight.math.Vector3f[] axes = {
                new thewall.engine.twilight.math.Vector3f(1, 0, 0).mult(xExtent),
                new thewall.engine.twilight.math.Vector3f(0, 1, 0).mult(yExtent),
                new thewall.engine.twilight.math.Vector3f(0, 0, 1).mult(zExtent)
        };

        thewall.engine.twilight.math.Vector3f nativeCenter = new thewall.engine.twilight.math.Vector3f(center.x, center.y, center.z);

        return new thewall.engine.twilight.math.Vector3f[]{
                nativeCenter.subtract(axes[0]).subtractLocal(axes[1]).subtractLocal(axes[2]),
                nativeCenter.add(axes[0]).subtractLocal(axes[1]).subtractLocal(axes[2]),
                nativeCenter.add(axes[0]).addLocal(axes[1]).subtractLocal(axes[2]),
                nativeCenter.subtract(axes[0]).addLocal(axes[1]).subtractLocal(axes[2]),
                nativeCenter.add(axes[0]).subtractLocal(axes[1]).addLocal(axes[2]),
                nativeCenter.subtract(axes[0]).subtractLocal(axes[1]).addLocal(axes[2]),
                nativeCenter.add(axes[0]).addLocal(axes[1]).addLocal(axes[2]),
                nativeCenter.subtract(axes[0]).addLocal(axes[1]).addLocal(axes[2])
        };


    }

    public final void updateGeometry(Vector3f center, float x, float y, float z) {
        if (center != null) {this.center.set(center); }
        this.xExtent = x;
        this.yExtent = y;
        this.zExtent = z;


        thewall.engine.twilight.math.Vector3f[] v = computeVertices();
        float[] vertices = new float[] {
                v[0].x, v[0].y, v[0].z, v[1].x, v[1].y, v[1].z, v[2].x, v[2].y, v[2].z, v[3].x, v[3].y, v[3].z, // back
                v[1].x, v[1].y, v[1].z, v[4].x, v[4].y, v[4].z, v[6].x, v[6].y, v[6].z, v[2].x, v[2].y, v[2].z, // right
                v[4].x, v[4].y, v[4].z, v[5].x, v[5].y, v[5].z, v[7].x, v[7].y, v[7].z, v[6].x, v[6].y, v[6].z, // front
                v[5].x, v[5].y, v[5].z, v[0].x, v[0].y, v[0].z, v[3].x, v[3].y, v[3].z, v[7].x, v[7].y, v[7].z, // left
                v[2].x, v[2].y, v[2].z, v[6].x, v[6].y, v[6].z, v[7].x, v[7].y, v[7].z, v[3].x, v[3].y, v[3].z, // top
                v[0].x, v[0].y, v[0].z, v[5].x, v[5].y, v[5].z, v[4].x, v[4].y, v[4].z, v[1].x, v[1].y, v[1].z  // bottom
        };


        //boxMesh.setVertices(Floats.asList(VERTICES));
        boxMesh.setVertices(Floats.asList(vertices));

        boxMesh.setIndices(Ints.asList(INDICES));

        /* geometry texture */
        ArrayList<Float> textureCoords = new ArrayList<>();
        for (float boxTextureCoordinate : TEXTURE_COORDS) {
            textureCoords.add(boxTextureCoordinate);
        }
        boxMesh.setTexture(textureCoords);

        /* normals geometry */
        ArrayList<Float> normalsVertex = new ArrayList<>();
        for (float normal : GEOMETRY_NORMALS_DATA) {
            normalsVertex.add(normal);
        }
        boxMesh.setNormals(normalsVertex);

        //this.boxMesh = new Mesh(Floats.asList(STATIC_BOX_POSITIONS), Arrays.asList(BOX_INDICES), textureCoords, normalsVertex);

        boxMesh.setName("Box");

        setMesh(boxMesh);
    }
}
