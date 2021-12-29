package thewall.engine.twilight.shaders;

import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import thewall.engine.twilight.material.Colour;
import thewall.engine.twilight.math.Maths;
import thewall.engine.twilight.spatials.Camera;
import thewall.engine.twilight.spatials.Light;

import java.util.List;

public interface ShaderHandle {
    void start();

    void stop();

    void init();

    void loadNumberOfRows(int numberOfRows);

    void loadOffset(Vector2f vector2f);

    void loadSkyColor(Colour colour);

    void loadTransformationMatrix(Matrix4f matrix4f);

    void loadShineVariables(float dumper, float reflectivity);

    void loadProjectionMatrix(Matrix4f matrix4f);

    void loadViewMatrix(Camera camera);

    void loadFakeLighting(boolean useFake);

    void loadLights(@NotNull List<Light> lights);
}
