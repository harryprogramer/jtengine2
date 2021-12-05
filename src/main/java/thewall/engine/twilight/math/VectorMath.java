package thewall.engine.twilight.math;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class VectorMath {
    private VectorMath(){

    }

    public static Vector3f subtract(@NotNull Vector3f source, Vector3f destination) {
        return new Vector3f(source.x() - destination.x(), source.y() - destination.y(), source.y() - destination.z());
    }

    public static Vector3f subtractLocal(Vector3f source, Vector3f destination) {
        if (null == source) {
            return null;
        }
        source.x -= destination.x;
        source.y -= destination.y;
        source.z -= destination.z;
        return source;
    }

    public static Vector3f add(@NotNull Vector3f source, Vector3f destination) {
        return new Vector3f(source.x + destination.x(), source.y + destination.y(), source.z + destination.z());
    }

    public static Vector3f addLocal(Vector3f source, Vector3f destination) {
        if (null == source) {
            return null;
        }
        source.x += destination.x;
        source.y += destination.y;
        source.z += destination.z;
        return source;
    }
}
