package jte2.engine.twilight.models.obj.mapped;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector3f;
import jte2.engine.twilight.errors.AssetsNotFoundException;
import jte2.engine.twilight.models.Mesh;
import jte2.engine.twilight.utils.Validation;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class NormalOBJLoader {
    private final static Logger logger = LogManager.getLogger(NormalOBJLoader.class);
    private static final String RES_LOC = "res/";


    public static Mesh loadOBJ(String objFileName) {
        Validation.checkNull(objFileName, "Filename is null");

        FileReader isr = null;
        File objFile = new File(RES_LOC + objFileName + ".obj");
        try {
            isr = new FileReader(objFile);
        } catch (FileNotFoundException e) {
            logger.error("OBJ file [{}] does not exist", objFileName);
            throw new AssetsNotFoundException("Asset not found: " + e.getMessage());
        }
        BufferedReader reader = new BufferedReader(isr);
        String line;
        List<VertexNM> vertices = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<Vector2f>();
        List<Vector3f> normals = new ArrayList<Vector3f>();
        List<Integer> indices = new ArrayList<Integer>();
        try {
            while (true) {
                line = reader.readLine();
                if (line.startsWith("v ")) {
                    String[] currentLine = line.split(" ");
                    Vector3f vertex = new Vector3f((float) Float.parseFloat(currentLine[1]),
                            (float) Float.parseFloat(currentLine[2]),
                            (float) Float.parseFloat(currentLine[3]));
                    VertexNM newVertex = new VertexNM(vertices.size(), vertex);
                    vertices.add(newVertex);

                } else if (line.startsWith("vt ")) {
                    String[] currentLine = line.split(" ");
                    Vector2f texture = new Vector2f((float) Float.valueOf(currentLine[1]),
                            (float) Float.valueOf(currentLine[2]));
                    textures.add(texture);
                } else if (line.startsWith("vn ")) {
                    String[] currentLine = line.split(" ");
                    Vector3f normal = new Vector3f((float) Float.valueOf(currentLine[1]),
                            (float) Float.valueOf(currentLine[2]),
                            (float) Float.valueOf(currentLine[3]));
                    normals.add(normal);
                } else if (line.startsWith("f ")) {
                    break;
                }
            }
            while (line != null && line.startsWith("f ")) {
                String[] currentLine = line.split(" ");
                String[] vertex1 = currentLine[1].split("/");
                String[] vertex2 = currentLine[2].split("/");
                String[] vertex3 = currentLine[3].split("/");
                VertexNM v0 = processVertex(vertex1, vertices, indices);
                VertexNM v1 = processVertex(vertex2, vertices, indices);
                VertexNM v2 = processVertex(vertex3, vertices, indices);
                calculateTangents(v0, v1, v2, textures);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Error reading the file");
        }
        removeUnusedVertices(vertices);
        float[] verticesArray = new float[vertices.size() * 3];
        float[] texturesArray = new float[vertices.size() * 2];
        float[] normalsArray = new float[vertices.size() * 3];
        float[] tangentsArray = new float[vertices.size() * 3];
        float furthest = convertDataToArrays(vertices, textures, normals, verticesArray,
                texturesArray, normalsArray, tangentsArray);
        int[] indicesArray = convertIndicesListToArray(indices);

        Mesh mesh = new Mesh();
        mesh.setVertices(verticesArray);
        mesh.setIndices(indicesArray);
        mesh.setTexture(texturesArray);
        mesh.setNormals(normalsArray);
        mesh.setTangents(tangentsArray);
        return mesh;

    }

    private static void calculateTangents(VertexNM v0, VertexNM v1, VertexNM v2,
                                          List<Vector2f> textures) {
        Vector3f delatPos1 = v1.getPosition().sub(v0.getPosition());
        Vector3f delatPos2 = v2.getPosition().sub(v0.getPosition());
        Vector2f uv0 = textures.get(v0.getTextureIndex());
        Vector2f uv1 = textures.get(v1.getTextureIndex());
        Vector2f uv2 = textures.get(v2.getTextureIndex());
        Vector2f deltaUv1 = uv1.sub(uv0);
        Vector2f deltaUv2 = uv2.sub(uv0);

        float r = 1.0f / (deltaUv1.x * deltaUv2.y - deltaUv1.y * deltaUv2.x);
        scale(delatPos1, deltaUv2.y);
        scale(delatPos2, deltaUv1.y);
        Vector3f tangent = delatPos1.sub(delatPos2);
        scale(tangent, r);
        v0.addTangent(tangent);
        v1.addTangent(tangent);
        v2.addTangent(tangent);
    }

    private static void scale(@NotNull Vector3f v, float scale){
        v.x *= scale;
        v.y *= scale;
        v.z *= scale;

    }

    private static VertexNM processVertex(String[] vertex, List<VertexNM> vertices,
                                          List<Integer> indices) {
        int index = Integer.parseInt(vertex[0]) - 1;
        VertexNM currentVertex = vertices.get(index);
        int textureIndex = Integer.parseInt(vertex[1]) - 1;
        int normalIndex = Integer.parseInt(vertex[2]) - 1;
        if (!currentVertex.isSet()) {
            currentVertex.setTextureIndex(textureIndex);
            currentVertex.setNormalIndex(normalIndex);
            indices.add(index);
            return currentVertex;
        } else {
            return dealWithAlreadyProcessedVertex(currentVertex, textureIndex, normalIndex, indices,
                    vertices);
        }
    }

    private static int[] convertIndicesListToArray(List<Integer> indices) {
        int[] indicesArray = new int[indices.size()];
        for (int i = 0; i < indicesArray.length; i++) {
            indicesArray[i] = indices.get(i);
        }
        return indicesArray;
    }

    private static float convertDataToArrays(List<VertexNM> vertices, List<Vector2f> textures,
                                             List<Vector3f> normals, float[] verticesArray, float[] texturesArray,
                                             float[] normalsArray, float[] tangentsArray) {
        float furthestPoint = 0;
        for (int i = 0; i < vertices.size(); i++) {
            VertexNM currentVertex = vertices.get(i);
            if (currentVertex.getLength() > furthestPoint) {
                furthestPoint = currentVertex.getLength();
            }
            Vector3f position = currentVertex.getPosition();
            Vector2f textureCoord = textures.get(currentVertex.getTextureIndex());
            Vector3f normalVector = normals.get(currentVertex.getNormalIndex());
            Vector3f tangent = currentVertex.getAverageTangent();
            verticesArray[i * 3] = position.x;
            verticesArray[i * 3 + 1] = position.y;
            verticesArray[i * 3 + 2] = position.z;
            texturesArray[i * 2] = textureCoord.x;
            texturesArray[i * 2 + 1] = 1 - textureCoord.y;
            normalsArray[i * 3] = normalVector.x;
            normalsArray[i * 3 + 1] = normalVector.y;
            normalsArray[i * 3 + 2] = normalVector.z;
            tangentsArray[i * 3] = tangent.x;
            tangentsArray[i * 3 + 1] = tangent.y;
            tangentsArray[i * 3 + 2] = tangent.z;

        }
        return furthestPoint;
    }

    private static VertexNM dealWithAlreadyProcessedVertex(VertexNM previousVertex, int newTextureIndex,
                                                           int newNormalIndex, List<Integer> indices, List<VertexNM> vertices) {
        if (previousVertex.hasSameTextureAndNormal(newTextureIndex, newNormalIndex)) {
            indices.add(previousVertex.getIndex());
            return previousVertex;
        } else {
            VertexNM anotherVertex = previousVertex.getDuplicateVertex();
            if (anotherVertex != null) {
                return dealWithAlreadyProcessedVertex(anotherVertex, newTextureIndex,
                        newNormalIndex, indices, vertices);
            } else {
                VertexNM duplicateVertex = new VertexNM(vertices.size(), previousVertex.getPosition());
                duplicateVertex.setTextureIndex(newTextureIndex);
                duplicateVertex.setNormalIndex(newNormalIndex);
                previousVertex.setDuplicateVertex(duplicateVertex);
                vertices.add(duplicateVertex);
                indices.add(duplicateVertex.getIndex());
                return duplicateVertex;
            }

        }
    }

    private static void removeUnusedVertices(List<VertexNM> vertices) {
        for (VertexNM vertex : vertices) {
            vertex.averageTangents();
            if (!vertex.isSet()) {
                vertex.setTextureIndex(0);
                vertex.setNormalIndex(0);
            }
        }
    }

}
