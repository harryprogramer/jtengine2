package thewall.engine.twilight.models;

import lombok.Getter;
import thewall.engine.twilight.utils.Validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static thewall.engine.twilight.utils.Validation.checkNull;


public class Mesh {
    private static final AtomicInteger counter = new AtomicInteger(-1);

    private int vaoID = -1;

    private int coordinatesSize = 3;

    private final List<Float> vertices;

    private final List<Integer> indices;

    private final List<Float> normals;

    private final List<Float> textureCoordinate;

    private String name = "Mesh-" + counter.getAndIncrement();


    public Mesh(){
        this.vertices = new ArrayList<>();
        this.indices = new ArrayList<>();
        this.normals = new ArrayList<>();
        this.textureCoordinate = new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    public Mesh(List<Float> vertices, List<Integer> indices, List<Float> normals, List<Float> textureCoordinates){
        checkNull(vertices, indices, normals, textureCoordinates);
        this.vertices = vertices;
        this.indices = indices;
        this.normals = normals;
        this.textureCoordinate = textureCoordinates;
        this.coordinatesSize = indices.size();
    }

    public void setVertices(List<Float> vertices){
        checkNull(vertices, "Vertices");
        this.vertices.clear();
        this.vertices.addAll(vertices);
    }

    public void setIndices(List<Integer> indices){
        checkNull(indices, "Indices");
        this.indices.clear();
        this.indices.addAll(indices);
        this.coordinatesSize = indices.size();
    }

    public void setNormals(List<Float> normals){
        checkNull(normals, "Normals");
        this.normals.clear();
        this.normals.addAll(normals);
    }

    public void setTextureCoordinates(List<Float> coordinates){
        checkNull(coordinates, "Texture coordinates");
        this.textureCoordinate.clear();
        this.textureCoordinate.addAll(coordinates);
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
        return vertices.size() == 0;
    }

    public boolean isIndicesZero(){
        return indices.size() == 0;
    }

    public boolean isNormalsZero(){
        return normals.size() == 0;
    }

    public boolean isTextureCoordinatesZero(){
        return textureCoordinate.size() == 0;
    }

    public List<Float> getVertices(){
        return vertices;
    }

    public List<Integer> getIndices(){
        return indices;
    }

    public List<Float> getNormals(){
        return normals;
    }

    public List<Float> getTextureCoordinates(){
        return textureCoordinate;
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
