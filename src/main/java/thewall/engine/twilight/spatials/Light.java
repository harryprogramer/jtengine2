package thewall.engine.twilight.spatials;

import lombok.Data;
import org.joml.Vector3f;
import thewall.engine.twilight.material.Colour;

@Data
public class Light {
    private final Vector3f position;
    private final Colour colour;
    private final Vector3f attenuation;
}
