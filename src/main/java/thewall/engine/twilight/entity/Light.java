package thewall.engine.twilight.entity;

import lombok.Data;
import org.joml.Vector3f;

@Data
public class Light {
    private final Vector3f position;
    private final Vector3f colour;
    private final Vector3f attenuation;
}
