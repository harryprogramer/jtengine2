package jte2.engine.twilight.shaders;

import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import jte2.engine.twilight.material.Colour;
import jte2.engine.twilight.spatials.Camera;
import jte2.engine.twilight.spatials.Light;

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
