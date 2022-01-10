package jte2.engine.twilight.models;

import com.google.common.primitives.Floats;
import com.google.common.primitives.Ints;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static jte2.engine.twilight.utils.Validation.checkNull;

@SuppressWarnings("unused")
public class Mesh {
    private static final AtomicInteger counter = new AtomicInteger(-1);

    private int vaoID = -1;

    private int coordinatesSize = 3;

    private float[] vertices = new float[0];

    private int[] indices = new int[0];

    private float[] normals = new float[0];

    private float[] textureCoordinate = new float[0];

    private float[] tangents = new float[0];

    private final ArrayList<Float> verticesList = new ArrayList<>();

    private final ArrayList<Integer> indicesList = new ArrayList<>();

    private final ArrayList<Float> normalsList = new ArrayList<>();

    private final ArrayList<Float> textureCoordinateList = new ArrayList<>();

    private final ArrayList<Float> tangentsList = new ArrayList<>();

    private String name = "Mesh-" + counter.getAndIncrement();

    public Mesh(){}

    public Mesh(List<Float> vertices, List<Integer> indices, List<Float> normals, List<Float> textureCoordinates){
        checkNull(vertices, indices, normals, textureCoordinates);

        this.verticesList.clear();
        this.verticesList.addAll(vertices);

        this.indicesList.clear();
        this.indicesList.addAll(indices);

        this.normalsList.clear();
        this.normalsList.addAll(normals);

        this.textureCoordinateList.clear();
        this.textureCoordinateList.addAll(textureCoordinates);

        this.coordinatesSize = indices.size();
    }

    public Mesh(float[] vertices, int[] indices, float[] normals, float[] textureCoordinates){
        checkNull("Mesh", vertices, indices, normals, textureCoordinates);

        this.vertices = vertices;
        this.indices = indices;
        this.normals = normals;
        this.textureCoordinate = textureCoordinates;

        this.coordinatesSize = indices.length;
    }

    private void convertListMesh(List<Float> vertices, List<Integer> indices, List<Float> normals, List<Float> textureCoordinates){
        this.vertices = Floats.toArray(vertices);
        this.indices = Ints.toArray(indices);
        this.normals = Floats.toArray(normals);
        this.textureCoordinate = Floats.toArray(textureCoordinates);
    }

    public void setVertices(float[] vertices){
        checkNull(vertices, "Vertices");
        this.vertices = vertices;
    }

    public void setIndices(int[] indices){
        checkNull(indices, "Indices");
        this.indices = indices;
        this.coordinatesSize = indices.length;
    }

    public void setNormals(float[] normals){
        checkNull(normals, "Normals is null");
        this.normals = normals;
    }

    public void setTexture(float[] coordinates){
        checkNull(coordinates, "Texture coordinates is null");
        this.textureCoordinate = coordinates;
    }

    public void setVertices(List<Float> vertices){
        checkNull(vertices, "Vertices is null");
        this.verticesList.clear();
        this.verticesList.addAll(vertices);
    }

    public void setIndices(List<Integer> indices){
        checkNull(indices, "Indices is null");
        this.indicesList.clear();
        this.indicesList.addAll(indices);
        this.coordinatesSize = indices.size();
    }

    public void setNormals(List<Float> normals){
        checkNull(normals, "Normals is null");
        this.normalsList.clear();
        this.normalsList.addAll(normals);
    }

    public void setTexture(List<Float> coordinates){
        checkNull(coordinates, "Texture coordinates is null");
        this.textureCoordinateList.clear();
        this.textureCoordinateList.addAll(coordinates);
    }

    public void setTangents(float[] tangents) {
        checkNull(tangents, "Tangents are null");
        this.tangents = tangents;
    }

    public void setTangents(List<Float> tangents){
        checkNull(tangents, "Tangents are null");
        this.tangentsList.clear();
        this.tangentsList.addAll(tangents);
    }

    public void setCoordinatesSize(int coordinatesSize) {
        this.coordinatesSize = coordinatesSize;
    }

    public void setID(int vao){
        if(vaoID != -1){
            throw new IllegalStateException("VAO is already set");
        }

        this.vaoID = vao;
    }

    public int getCoordinatesSize(){
        return coordinatesSize;
    }

    public int getID(){
        return vaoID;
    }

    public boolean isVerticesZero(){
        return vertices.length == 0;
    }

    public boolean isIndicesZero(){
        return indices.length == 0;
    }

    public boolean isNormalsZero(){
        return normals.length == 0;
    }

    public boolean isTextureCoordinatesZero(){
        return textureCoordinate.length == 0;
    }

    public List<Float> getTangents() {
        if(tangentsList.size() == 0){
            if(tangents.length == 0){
                return tangentsList;
            }
            tangentsList.addAll(Floats.asList(tangents));
        }
        return tangentsList;
    }

    public List<Float> getVertices(){
        if(verticesList.size() == 0){
            if(vertices.length == 0){
                return verticesList;
            }
            verticesList.addAll(Floats.asList(vertices));
        }
        return verticesList;
    }

    public List<Integer> getIndices(){
        if(indicesList.size() == 0){
            if(indices.length == 0){
                return indicesList;
            }

            indicesList.addAll(Ints.asList(indices));
        }

        return indicesList;
    }

    public List<Float> getNormals(){
        if(normalsList.size() == 0){
            if(normals.length == 0){
                return normalsList;
            }

            normalsList.addAll(Floats.asList(normals));
        }

        return normalsList;
    }

    public List<Float> getTextureCoordinates(){
        if(textureCoordinateList.size() == 0){
            if(textureCoordinate.length == 0){
                return textureCoordinateList;
            }

            textureCoordinateList.addAll(Floats.asList(textureCoordinate));
        }

        return textureCoordinateList;
    }

    public float[] getVerticesArray(){
        if(vertices.length == 0){
            if(verticesList.size() == 0){
                return vertices;
            }

            vertices = Floats.toArray(verticesList);
        }

        return vertices;
    }

    public int[] getIndicesArray(){
        if(indices.length == 0){
            if(indicesList.size() == 0){
                return indices;
            }

            indices = Ints.toArray(indicesList);
        }

        return indices;
    }

    public float[] getNormalsArray(){
        if(normals.length == 0){
            if(normalsList.size() == 0){
                return normals;
            }

            normals = Floats.toArray(normalsList);
        }

        return normals;
    }

    public float[] getTextureArray(){
        if(textureCoordinate.length == 0){
            if(textureCoordinateList.size() == 0){
                return textureCoordinate;
            }

            textureCoordinate = Floats.toArray(textureCoordinateList);
        }

        return textureCoordinate;
    }

    public float[] getTangentsArray() {
        if(tangents.length == 0){
            if(tangentsList.size() == 0){
                return tangents;
            }

            tangents = Floats.toArray(tangentsList);
        }

        return tangents;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        checkNull(getClass(), name);
        this.name = name;
    }
}
