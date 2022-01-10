package jte2.engine.twilight.models;

import jte2.engine.twilight.spatials.Spatial;
import jte2.engine.twilight.texture.ModelTexture;
import lombok.Data;

@Data
public class TexturedModel {
    private final Spatial model;
    private final ModelTexture modelTexture;
}
