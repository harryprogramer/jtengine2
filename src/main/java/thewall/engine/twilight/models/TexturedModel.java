package thewall.engine.twilight.models;

import thewall.engine.twilight.entity.Spatial;
import thewall.engine.twilight.texture.ModelTexture;
import lombok.Data;

@Data
public class TexturedModel {
    private final Spatial model;
    private final ModelTexture modelTexture;
}
