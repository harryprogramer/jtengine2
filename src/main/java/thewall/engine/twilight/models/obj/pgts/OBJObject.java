package thewall.engine.twilight.models.obj.pgts;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * OBJ parser
 *
 * @author many
 * */

public class OBJObject {
    private static final String POSITION_VERTEX_SYNTAX =            "v";
    private static final String VERTEX_TEXTURE_POSITION_SYNTAX =    "vt";
    private static final String NORMAL_VERTEX_SYNTAX =              "vn";
    private static final String FACE_SYNTAX =                       "f";
    private static final String OBJECT_NAME_SYNTAX =                "o";
    private static final String SMOOTH_SHADING_SYNTAX =             "s";

    private String objName = "N/A";

    private boolean smoothShading;

    private List<String> objContext;
    @Getter
    private final List<Vector3f> vertexPositions = new ArrayList<>();
    @Getter
    private final List<Vector3f> normalVectors = new ArrayList<>();
    @Getter
    private final List<Vector2f> textureCoordinates = new ArrayList<>();
    @Getter
    private final List<ObjectFace> modelFaces = new ArrayList<>();

    public OBJObject(File file){
        try {
            loadContextFromFile(file);
        }catch (Exception e){
            throw new OBJException(e.getMessage());
        }

        parseAll();
    }

    public OBJObject(String context){
        try{
            loadStringContext(context);
        }catch (Exception e){
            throw new OBJException(e.getMessage());
        }

        parseAll();
    }

    private boolean isSmoothShadingSupported(){
        return smoothShading;
    }

    private void loadContextFromFile(@NotNull File file) throws IOException {
        objContext = Files.readAllLines(file.toPath());
    }

    private void loadStringContext(@NotNull String context){
        String[] arr = context.split("\\s*\n\\s*");
        objContext = Arrays.asList(arr);
    }

    private void parseAll() throws OBJSyntaxParseException {
        int lineIndex = 0;

        for(String line : objContext){
            lineIndex++;

            if(line.charAt(0) == '#'){
                continue;
            }

            String[] lineArgs = line.split(" ");

            switch (lineArgs[0]) {
                case POSITION_VERTEX_SYNTAX -> {
                    float x, y, z;

                    if (lineArgs.length != 4) {
                        throw new OBJSyntaxParseException("Line [" + lineIndex + "] have too many or too few arguments");
                    }

                    try {
                        x = Float.parseFloat(lineArgs[1]);
                    } catch (Exception e) {
                        throw new OBJSyntaxParseException("Invaild vector x float value on line [" + lineIndex + "], " + e.getMessage());
                    }

                    try {
                        y = Float.parseFloat(lineArgs[2]);
                    } catch (Exception e) {
                        throw new OBJSyntaxParseException("Invaild vector y float value on line [" + lineIndex + "], " + e.getMessage());
                    }

                    try {
                        z = Float.parseFloat(lineArgs[3]);
                    } catch (Exception e) {
                        throw new OBJSyntaxParseException("Invaild vector z float value on line [" + lineIndex + "], " + e.getMessage());
                    }

                    loadVertexPosition(x, y, z);

                }
                case VERTEX_TEXTURE_POSITION_SYNTAX -> {
                    float x, y;

                    try {
                        x = Float.parseFloat(lineArgs[1]);
                    } catch (Exception e) {
                        throw new OBJSyntaxParseException("Invaild texture vertex x float value on line [" + lineIndex + "], " + e.getMessage());
                    }

                    try {
                        y = Float.parseFloat(lineArgs[2]);
                    } catch (Exception e) {
                        throw new OBJSyntaxParseException("Invaild texture vertex y float value on line [" + lineIndex + "], " + e.getMessage());
                    }

                    loadTextureCoordinates(x, y);

                }
                case NORMAL_VERTEX_SYNTAX -> {
                    float x, y, z;

                    try {
                        x = Float.parseFloat(lineArgs[1]);
                    } catch (Exception e) {
                        throw new OBJSyntaxParseException("Invaild normal vertex x float value on line [" + lineIndex + "], " + e.getMessage());
                    }

                    try {
                        y = Float.parseFloat(lineArgs[2]);
                    } catch (Exception e) {
                        throw new OBJSyntaxParseException("Invaild normal vertex y float value on line [" + lineIndex + "], " + e.getMessage());
                    }

                    try {
                        z = Float.parseFloat(lineArgs[3]);
                    } catch (Exception e) {
                        throw new OBJSyntaxParseException("Invaild normal vertex z float value on line [" + lineIndex + "], " + e.getMessage());
                    }

                    loadNormalVertex(x, y, z);

                }
                case FACE_SYNTAX -> {
                    List<VertexCoord> vertexCoords = new ArrayList<>();

                    for(int i = 0; i < 3; i++){
                        String coordinate = lineArgs[i + 1];

                        int index = -1;
                        int argIndex = 1;
                        String unpV = "", unpVT = "", unpVN = "";
                        for(int l = 0; l < coordinate.length(); l++){
                            index++;
                            if(coordinate.charAt(index) != '/') {
                                if(!checkNumeric(coordinate.charAt(index)))
                                    throw new OBJSyntaxParseException("Sign of number was expected but letter was found, on line [" + lineIndex + "]");
                                if(argIndex == 1){
                                    unpV += coordinate.charAt(index);
                                }else if(argIndex == 2){
                                    unpVT += coordinate.charAt(index);
                                }else if(argIndex == 3){
                                    unpVN += coordinate.charAt(index);
                                    break;
                                }else {
                                    throw new IllegalStateException();
                                }
                            }else {
                                argIndex++;
                            }
                        }
                        vertexCoords.add(new VertexCoord(Integer.parseInt(unpV), Integer.parseInt(unpVT), Integer.parseInt(unpVN)));
                    }
                    modelFaces.add(new ObjectFace(vertexCoords.toArray(new VertexCoord[2])));
                }
                case OBJECT_NAME_SYNTAX -> objName = lineArgs[1];
                case SMOOTH_SHADING_SYNTAX -> smoothShading = !lineArgs[1].equalsIgnoreCase("off");
                default -> throw new OBJSyntaxParseException("Unknown start syntax on line [" + lineIndex + "], [" + lineArgs[0] + "]");
            }
        }
        if(!smoothShading) {
            Logger.getLogger("thewall.game.engine.models.obj.pgts.OBJObject").log(Level.WARNING, "This thewall.game.engine is support only smooth shading. " +
                    "Model [" + objName + "] is doesn't support it");
        }
    }

    private void loadVertexPosition(float x, float y, float z){
        vertexPositions.add(new Vector3f(x, y, z));
    }

    private void loadTextureCoordinates(float x, float y){
        textureCoordinates.add(new Vector2f(x, y));
    }

    private void loadNormalVertex(float x, float y, float z){
        normalVectors.add(new Vector3f(x, y, z));
    }


    private boolean checkNumeric(char character){
        char[] numbers = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

        for (char number : numbers) {
            if (character == number) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        return "OBJObject{" +
                "objName='" + objName + '\'' +
                ", vertexPositions=" + vertexPositions +
                ", normalVectors=" + normalVectors +
                ", textureCoordinates=" + textureCoordinates +
                '}';
    }
}
